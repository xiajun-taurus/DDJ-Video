package com.xiajun.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiajun.enums.ThumbnailTypeEnum;
import com.xiajun.enums.VideoStatusEnum;
import com.xiajun.pojo.Bgm;
import com.xiajun.pojo.Comments;
import com.xiajun.pojo.Videos;
import com.xiajun.pojo.vo.CommentsVO;
import com.xiajun.pojo.vo.VideosVO;
import com.xiajun.service.impl.BgmServiceImpl;
import com.xiajun.service.impl.VideosServiceImpl;
import com.xiajun.utils.FFMpeg;
import com.xiajun.utils.JSONResult;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * <p>
 * Video前端控制器
 * </p>
 *
 * @author xiajun
 * @since 2019-05-20
 */
@RestController
@Slf4j
@Api(value = "视频相关业务接口", tags = {"视频相关业务controller"})
@RequestMapping("video")
public class VideoController extends BasicController {
    @Autowired
    private BgmServiceImpl bgmService;
    @Autowired
    private VideosServiceImpl videosService;

    @Value("${thumbnailType}")
    private String thumbnailType;

    @ApiOperation(value = "用户上传视频", notes = "用户上传视频接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", required = true,
                    dataType = "String", paramType = "form", dataTypeClass = String.class),
            @ApiImplicitParam(name = "bgmId", value = "背景音乐id", required = false,
                    dataType = "String", paramType = "form", dataTypeClass = String.class),
            @ApiImplicitParam(name = "videoSeconds", value = "背景音乐播放长度", required = true,
                    dataType = "String", paramType = "form", dataTypeClass = String.class),
            @ApiImplicitParam(name = "videoWidth", value = "视频宽度", required = true,
                    dataType = "String", paramType = "form", dataTypeClass = String.class),
            @ApiImplicitParam(name = "videoHeight", value = "视频高度", required = true,
                    dataType = "String", paramType = "form", dataTypeClass = String.class),
            @ApiImplicitParam(name = "desc", value = "视频描述", required = false,
                    dataType = "String", paramType = "form", dataTypeClass = String.class)
    })
    @PostMapping(value = "/upload", headers = "content-type=multipart/form-data")
    public JSONResult upload(String userId, String bgmId, Double videoSeconds, Integer videoWidth, Integer videoHeight,
                             String desc, @ApiParam(value = "短视频", required = true) MultipartFile file) throws Exception {

        if (StringUtils.isBlank(userId)) {
            return JSONResult.errorMsg("用户id不能为空");
        }

        // 文件保存的命名空间
//		String fileSpace = "C:/imooc_videos_dev";
        // 保存到数据库中的相对路径
        String uploadPath = new StringBuilder()
                .append(File.separator)
                .append(userId)
                .append(File.separator)
                .append("video")
                .toString();
        // 封面与视频在同一个目录
        String coverPath = uploadPath;

        FileOutputStream fileOutputStream = null;
        InputStream inputStream = null;
        // 文件上传的最终保存路径
        String finalVideoPath = "";
        try {
            if (file != null) {

                String fileName = file.getOriginalFilename();
                // abc.mp4，去掉微信小程序上传产生的..标示
                String arrayFilenameItem[] =  fileName.split("\\.");
                String fileNamePrefix = "";
                for (int i = 0 ; i < arrayFilenameItem.length-1 ; i ++) {
                    fileNamePrefix += arrayFilenameItem[i];
                }

                if (StringUtils.isNotBlank(fileName)) {

                    finalVideoPath = FILE_SPACE + uploadPath + File.separator + fileName;
                    // 设置数据库保存的路径
                    uploadPath += (File.separator + fileName);

                    coverPath = coverPath + File.separator + fileNamePrefix + ThumbnailTypeEnum.getExtensionNameByType(thumbnailType);

                    File outFile = new File(finalVideoPath);
                    // 若目录不存在创建目录
                    if (outFile.getParentFile() != null || !outFile.getParentFile().isDirectory()) {
                        // 创建父文件夹
                        outFile.getParentFile().mkdirs();
                    }

                    fileOutputStream = new FileOutputStream(outFile);
                    inputStream = file.getInputStream();
                    IOUtils.copy(inputStream, fileOutputStream);
                }

            } else {
                return JSONResult.errorMsg("上传出错...");
            }
        } catch (Exception e) {
            log.error("上传出错...{}", e.getMessage());
            return JSONResult.errorMsg("上传出错...");
        } finally {
            if (fileOutputStream != null) {
                fileOutputStream.flush();
                fileOutputStream.close();
            }
        }

        // 判断bgmId是否为空，如果不为空，
        // 那就查询bgm的信息，并且合并视频，生产新的视频
        if (StringUtils.isNotBlank(bgmId)) {
            Bgm bgm = bgmService.getById(bgmId);
            String mp3InputPath = FILE_SPACE + bgm.getPath();

            String videoInputPath = finalVideoPath;

            String videoOutputName = UUID.randomUUID().toString() + ".mp4";
            uploadPath = File.separator
                    + userId
                    + File.separator
                    + "video"
                    + File.separator
                    + videoOutputName;
            finalVideoPath = FILE_SPACE + uploadPath;
            FFMpeg.mergeVideoAndBgm(videoInputPath, mp3InputPath, videoSeconds, finalVideoPath);
        }
        System.out.println("uploadPathDB=" + uploadPath);
        System.out.println("finalVideoPath=" + finalVideoPath);

        // 对视频进行截图
        if (ThumbnailTypeEnum.GIF.getType().equals(thumbnailType)) {
            FFMpeg.getGIFCover(finalVideoPath, FILE_SPACE + coverPath);
        } else if (ThumbnailTypeEnum.JPEG.getType().equals(thumbnailType)) {
            FFMpeg.getImgCover(finalVideoPath, FILE_SPACE + coverPath);
        } else {
            log.error("错误的封面类型:{0}", thumbnailType);
            return JSONResult.errorMsg("错误的封面类型...");
        }

        // 数据入库
        Videos video = new Videos();
        video.setAudioId(bgmId)
                .setUserId(userId)
                .setVideoSeconds(videoSeconds.floatValue())
                .setVideoHeight(videoHeight)
                .setVideoWidth(videoWidth)
                .setVideoDesc(desc)
                .setVideoPath(uploadPath)
                .setCoverPath(coverPath)
                .setStatus(VideoStatusEnum.SUCCESS.value)
                .setCreateTime(LocalDateTime.now());
        videosService.save(video);

        return JSONResult.ok(video.getId());
    }

    /**
     *
     * @Description: 分页和搜索查询视频列表
     * isSaveRecord：1 - 需要保存
     * 				 0 - 不需要保存 ，或者为空的时候
     */
    @PostMapping(value="/showAll")
    public JSONResult showAll(@RequestBody Videos video, Integer isSaveRecord,
                                   Integer page, Integer pageSize) throws Exception {

        if (page == null) {
            page = 1;
        }

        if (pageSize == null) {
            pageSize = PAGE_SIZE;
        }
        Page<VideosVO> allVideos = videosService.getAllVideos(video, isSaveRecord, page, pageSize);
        return JSONResult.ok(allVideos);
    }

    /**
     * @Description: 我关注的人发的视频
     */
    @PostMapping("/showMyFollow")
    public JSONResult showMyFollow(String userId, Integer page) throws Exception {

        if (StringUtils.isBlank(userId)) {
            return JSONResult.ok();
        }

        if (page == null) {
            page = 1;
        }

        int pageSize = 6;

        Page<VideosVO> videosVOPage = videosService.queryMyFollowVideos(userId, page, pageSize);

        return JSONResult.ok(videosVOPage);
    }

    /**
     * @Description: 我收藏(点赞)过的视频列表
     */
    @PostMapping("/showMyLike")
    public JSONResult showMyLike(String userId, Integer page, Integer pageSize) throws Exception {

        if (StringUtils.isBlank(userId)) {
            return JSONResult.ok();
        }

        if (page == null) {
            page = 1;
        }

        if (pageSize == null) {
            pageSize = 6;
        }
        Page<VideosVO> videosVOPage = videosService.queryMyLikeVideos(userId, page, pageSize);

        return JSONResult.ok(videosVOPage);
    }

    @PostMapping(value="/hot")
    public JSONResult hot() throws Exception {
        return JSONResult.ok(videosService.getHotwords());
    }

    @PostMapping(value="/userLike")
    public JSONResult userLike(String userId, String videoId, String videoCreaterId)
            throws Exception {
        videosService.userLikeVideo(userId, videoId, videoCreaterId);
        return JSONResult.ok();
    }

    @PostMapping(value="/userUnLike")
    public JSONResult userUnLike(String userId, String videoId, String videoCreaterId) throws Exception {
        videosService.userUnLikeVideo(userId, videoId, videoCreaterId);
        return JSONResult.ok();
    }

    @PostMapping("/saveComment")
    public JSONResult saveComment(@RequestBody Comments comment,
                                       String fatherCommentId, String toUserId) throws Exception {

        comment.setFatherCommentId(fatherCommentId);
        comment.setToUserId(toUserId);

        videosService.saveComment(comment);
        return JSONResult.ok();
    }

    @PostMapping("/getVideoComments")
    public JSONResult getVideoComments(String videoId, Integer page, Integer pageSize) throws Exception {

        if (StringUtils.isBlank(videoId)) {
            return JSONResult.ok();
        }

        // 分页查询视频列表，时间顺序倒序排序
        if (page == null) {
            page = 1;
        }

        if (pageSize == null) {
            pageSize = 10;
        }

        Page<CommentsVO> list = videosService.getAllComments(videoId, page, pageSize);

        return JSONResult.ok(list);
    }
}