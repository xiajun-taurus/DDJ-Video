package com.xiajun.controller;

import com.xiajun.pojo.Users;
import com.xiajun.pojo.UsersReport;
import com.xiajun.pojo.vo.PublisherVideo;
import com.xiajun.pojo.vo.UsersVO;
import com.xiajun.service.impl.UsersServiceImpl;
import com.xiajun.utils.JSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * <p>
 * User前端控制器
 * </p>
 *
 * @author xiajun
 * @since 2019-05-20
 */
@RestController
@RequestMapping("user")
@Api(value = "用户相关业务接口", tags = {"用户相关业务controllrt"})
public class UserController extends BasicController {
    @Autowired
    private UsersServiceImpl usersService;

    @ApiOperation(value = "用户上传头像", notes = "用户上传头像接口")
    @PostMapping("/uploadFace")
    public JSONResult uploadFace(String userId, @RequestParam("file") MultipartFile[] files) throws Exception {
        if (StringUtils.isBlank(userId)) {
            return JSONResult.errorMsg("用户id不能为空");
        }

        //1.定义一个路径
        String fileSpace = "/Users/xiajun/03.Workspaces/ddj-video-files";
        //保存到数据库中的相对路径
        String uploadPathDB = "/" + userId + "/face";

        FileOutputStream fileOutputStream = null;
        InputStream inputStream = null;
        try {
            if (files != null && files.length > 0) {

                String filename = files[0].getOriginalFilename();
                if (StringUtils.isNotBlank(filename)) {
                    //文件上传的最终保存路径--绝对路径
                    String finalFacePath = fileSpace + uploadPathDB + "/" + filename;
                    uploadPathDB += ("/" + filename);
                    File outFile = new File(finalFacePath);
                    if (outFile.getParentFile() != null || !outFile.getParentFile().isDirectory()) {
                        //创建父文件夹
                        outFile.getParentFile().mkdirs();
                    }
                    fileOutputStream = new FileOutputStream(outFile);
                    inputStream = files[0].getInputStream();
                    IOUtils.copy(inputStream, fileOutputStream);
                } else {
                    return JSONResult.errorMsg("上传出错");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return JSONResult.errorMsg("上传出错");
        } finally {
            if (fileOutputStream != null) {
                fileOutputStream.flush();
                fileOutputStream.close();
            }
        }
        Users user = new Users();
        user.setId(userId)
                .setFaceImage(uploadPathDB);
        usersService.updateById(user);
        return JSONResult.ok(uploadPathDB);
    }

    @ApiOperation(value = "查询用户信息", notes = "查询用户信息接口")
    @PostMapping("/query")
    public JSONResult query(String userId,String fanId) {
        if (StringUtils.isBlank(userId)) {
            return JSONResult.errorMsg("用户id不能为空");
        }
        Users user = usersService.getById(userId);
        UsersVO usersVO = new UsersVO();
        BeanUtils.copyProperties(user, usersVO);
        usersVO.setFollow(usersService.queryIfFollow(userId,fanId));
        return JSONResult.ok(usersVO);
    }

    @PostMapping("/queryPublisher")
    public JSONResult queryPublisher(String loginUserId, String videoId,
                                     String publishUserId) throws Exception {

        if (StringUtils.isBlank(publishUserId)) {
            return JSONResult.errorMsg("");
        }

        // 1. 查询视频发布者的信息
        Users userInfo = usersService.getById(publishUserId);
        UsersVO publisher = new UsersVO();
        BeanUtils.copyProperties(userInfo, publisher);

        // 2. 查询当前登录者和视频的点赞关系
        boolean userLikeVideo = usersService.isUserLikeVideo(loginUserId, videoId);

        PublisherVideo bean = new PublisherVideo();
        bean.setPublisher(publisher);
        bean.setUserLikeVideo(userLikeVideo);

        return JSONResult.ok(bean);
    }

    @PostMapping("/beyourfans")
    public JSONResult beyourfans(String userId, String fanId) throws Exception {

        if (StringUtils.isBlank(userId) || StringUtils.isBlank(fanId)) {
            return JSONResult.errorMsg("");
        }

        usersService.saveUserFanRelation(userId, fanId);

        return JSONResult.ok("关注成功...");
    }

    @PostMapping("/dontbeyourfans")
    public JSONResult dontbeyourfans(String userId, String fanId) throws Exception {

        if (StringUtils.isBlank(userId) || StringUtils.isBlank(fanId)) {
            return JSONResult.errorMsg("");
        }

        usersService.deleteUserFanRelation(userId, fanId);

        return JSONResult.ok("取消关注成功...");
    }

    @PostMapping("/reportUser")
    public JSONResult reportUser(@RequestBody UsersReport usersReport) throws Exception {

        // 保存举报信息
        usersService.reportUser(usersReport);

        return JSONResult.errorMsg("举报成功...有你平台变得更美好...");
    }

}