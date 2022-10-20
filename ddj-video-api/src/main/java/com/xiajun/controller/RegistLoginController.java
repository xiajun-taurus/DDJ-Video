package com.xiajun.controller;

import com.xiajun.pojo.Users;
import com.xiajun.pojo.vo.UsersVO;
import com.xiajun.service.impl.UsersServiceImpl;
import com.xiajun.utils.JSONResult;
import com.xiajun.utils.MD5Utils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * <p>
 * RegistLogin前端控制器
 * </p>
 *
 * @author xiajun
 * @since 2019-05-20
 */
@RestController
@Api(value = "用户注册登录接口", tags = {"注册和登录controllrt"})
public class RegistLoginController extends BasicController {
    @Autowired
    private UsersServiceImpl usersService;

    @ApiOperation(value = "用户注册", notes = "用户注册接口")
    @PostMapping("/regist")
    public JSONResult regist(@RequestBody Users user) throws Exception {
        String username = user.getUsername();
        String password = user.getPassword();
        //1. 判断用户和密码必须不为空
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            return JSONResult.errorMsg("用户名和密码不能为空");
        }
        //2. 判断用户名是否存在
        boolean exist = usersService.queryUserIsExist(username);
        //3. 保存用户，注册信息
        if (!exist) {
            user.setNickname(username)
                    .setPassword(MD5Utils.getMD5Str(password))
                    .setFansCounts(0)
                    .setReceiveLikeCounts(0)
                    .setFollowCounts(0);
            usersService.save(user);
        } else {
            return JSONResult.errorMsg("用户名已存在，换一个试试");
        }
        //密码不传回前端
        user.setPassword("");
        UsersVO userVO = setUserRedisToken(user);
        return JSONResult.ok(userVO);
    }

    @ApiOperation(value = "用户登录", notes = "用户登录接口")
    @PostMapping("/login")
    public JSONResult login(@RequestBody Users user) throws Exception {
        String username = user.getUsername();
        String password = user.getPassword();
        //1. 判断用户和密码必须不为空
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            return JSONResult.errorMsg("用户名和密码不能为空");
        }
        //2. 判断用户名是否存在
        Users dbUser = usersService.queryUserForLogin(username, password);
        //3. 返回
        if (dbUser != null) {
            user.setPassword("");
            UsersVO userVO = setUserRedisToken(dbUser);
            return JSONResult.ok(userVO);
        }
        return JSONResult.errorMsg("用户名或密码错误，请重试");
    }

    @ApiOperation(value = "用户注销", notes = "用户注销接口")
    @ApiImplicitParam(name = "userId", value = "用户id", required = true,
            dataType = "String", paramType = "query", dataTypeClass = String.class)
    @PostMapping("/logout")
    public JSONResult login(String userId) throws Exception {
        redisOperator.del(USER_REDIS_SESSION + ":" + userId);
        return JSONResult.ok();
    }

    private UsersVO setUserRedisToken(Users userModel) {
        String uniqueToken = UUID.randomUUID().toString();
        redisOperator.set(USER_REDIS_SESSION + ":" + userModel.getId(), uniqueToken, 1000 * 30);
        UsersVO userVO = new UsersVO();
        BeanUtils.copyProperties(userModel, userVO);
        userVO.setUserToken(uniqueToken);
        return userVO;
    }
}