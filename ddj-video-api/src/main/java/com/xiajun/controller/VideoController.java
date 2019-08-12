package com.xiajun.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
@Api(value = "视频相关业务接口", tags = {"视频相关业务controller"})
@RequestMapping("video")
public class VideoController extends BasicController {
    @Autowired
    private BgmServiceImpl bgmService;
    @Autowired
    private VideosServiceImpl videosService;

    @ApiOperation(value = "用户上传视频", notes = "用户上传视频接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", required = true,
                    dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "bgmId", value = "背景音乐id", required = false,
                    dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "videoSeconds", value = "背景音乐播放长度", required = true,
                    dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "videoWidth", value = "视频宽度", required = true,
                    dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "videoHeight", value = "视频高度", required = true,
                    dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "desc", value = "视频描述", required = false,
                    dataType = "String", paramType = "form")
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
        String uploadPathDB = "/" + userId + "/video";
        String coverPathDB = "/" + userId + "/video";

        FileOutputStream fileOutputStream = null;
        InputStream inputStream = null;
        // 文件上传的最终保存路径
        String finalVideoPath = "";
        try {
            if (file != null) {

                String fileName = file.getOriginalFilename();
                // abc.mp4
                String arrayFilenameItem[] =  fileName.split("\\.");
                String fileNamePrefix = "";
                for (int i = 0 ; i < arrayFilenameItem.length-1 ; i ++) {
                    fileNamePrefix += arrayFilenameItem[i];
                }
                // fix bug: 解决小程序端OK，PC端不OK的bug，原因：PC端和小程序端对临时视频的命名不同
//				String fileNamePrefix = fileName.split("\\.")[0];

                if (StringUtils.isNotBlank(fileName)) {

                    finalVideoPath = FILE_SPACE + uploadPathDB + "/" + fileName;
                    // 设置数据库保存的路径
                    uploadPathDB += ("/" + fileName);
                    coverPathDB = coverPathDB + "/" + fileNamePrefix + ".gif";

                    File outFile = new File(finalVideoPath);
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
            e.printStackTrace();
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
            uploadPathDB = "/" + userId + "/video" + "/" + videoOutputName;
            finalVideoPath = FILE_SPACE + uploadPathDB;
            FFMpeg.mergeVideoAndBgm(videoInputPath, mp3InputPath, videoSeconds, finalVideoPath);
        }
        System.out.println("uploadPathDB=" + uploadPathDB);
        System.out.println("finalVideoPath=" + finalVideoPath);

        // 对视频进行截图
        FFMpeg.getGIFCover(finalVideoPath, FILE_SPACE + coverPathDB);


        Videos video = new Videos();
        video.setAudioId(bgmId)
                .setUserId(userId)
                .setVideoSeconds(videoSeconds.floatValue())
                .setVideoHeight(videoHeight)
                .setVideoWidth(videoWidth)
                .setVideoDesc(desc)
                .setVideoPath(uploadPathDB)
                .setCoverPath(coverPathDB)
                .setStatus(VideoStatusEnum.SUCCESS.value)
                .setCreateTime(LocalDateTime.now());
        videosService.save(video);

        return JSONResult.ok(video.getId());
    }


//    @ApiOperation(value = "用户上传视频封面", notes = "用户上传视频封面接口")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "userId", value = "用户id", required = true,
//                    dataType = "String", paramType = "form"),
//            @ApiImplicitParam(name = "videoId", value = "视频主键id", required = true,
//                    dataType = "String", paramType = "form")
//    })
//    @PostMapping(value = "/uploadCover", headers = "content-type=multipart/form-data")
//    public JSONResult upload(String userId, String videoId, @ApiParam(value = "短视频封面", required = true) MultipartFile file) throws Exception {
//        if (StringUtils.isBlank(videoId) || StringUtils.isBlank(userId)) {
//            return JSONResult.errorMsg("视频主键id和用户id不能为空");
//        }
//
//        //保存到数据库中的相对路径
//        String uploadPathDB = "/" + userId + "/video";
//
//        FileOutputStream fileOutputStream = null;
//        InputStream inputStream = null;
//        String finalCoverPath = null;
//        try {
//            if (file != null) {
//                String filename = file.getOriginalFilename();
//                if (StringUtils.isNotBlank(filename)) {
//                    //文件上传的最终保存路径--绝对路径
//                    finalCoverPath = FILE_SPACE + "/" + uploadPathDB + "/" + filename;
//                    uploadPathDB += ("/" + filename);
//                    File outFile = new File(finalCoverPath);
//                    if (outFile.getParentFile() != null || !outFile.getParentFile().isDirectory()) {
//                        //创建父文件夹
//                        outFile.getParentFile().mkdirs();
//                    }
//                    fileOutputStream = new FileOutputStream(outFile);
//                    inputStream = file.getInputStream();
//                    IOUtils.copy(inputStream, fileOutputStream);
//                } else {
//                    return JSONResult.errorMsg("上传出错");
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//            return JSONResult.errorMsg("上传出错");
//        } finally {
//            if (fileOutputStream != null) {
//                fileOutputStream.flush();
//                fileOutputStream.close();
//            }
//        }
//        VideosVO video = new VideosVO();
//        video.setId(videoId).setCoverPath(uploadPathDB);
//        videosService.updateById(video);
//        return JSONResult.ok();
//    }
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