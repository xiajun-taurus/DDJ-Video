package com.xiajun.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiajun.mapper.UsersMapper;
import com.xiajun.pojo.Users;
import com.xiajun.pojo.UsersFans;
import com.xiajun.pojo.UsersLikeVideos;
import com.xiajun.pojo.UsersReport;
import com.xiajun.service.IUsersFansService;
import com.xiajun.service.IUsersLikeVideosService;
import com.xiajun.service.IUsersReportService;
import com.xiajun.service.IUsersService;
import com.xiajun.utils.MD5Utils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author xiajun
 * @since 2019-05-25
 */
@Service
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users> implements IUsersService {
    @Autowired
    private IUsersLikeVideosService usersLikeVideosService;
    @Autowired
    private IUsersFansService usersFansService;
    @Autowired
    private IUsersReportService usersReportService;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public boolean queryUserIsExist(String username) {
        final LambdaQueryWrapper<Users> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Users::getUsername, username);
        return Optional.ofNullable(getOne(wrapper)).isPresent();
    }

    @Override
    public Users queryUserForLogin(String username, String password) throws Exception {
        final LambdaQueryWrapper<Users> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Users::getUsername, username)
                .eq(Users::getPassword, MD5Utils.getMD5Str(password));
        return getOne(wrapper);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public boolean save(Users entity) {
        return super.save(entity);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Users getById(Serializable id) {
        return super.getById(id);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public boolean isUserLikeVideo(String userId, String videoId) {
        if (StringUtils.isBlank(userId) || StringUtils.isBlank(videoId)) {
            return false;
        }
        final LambdaQueryWrapper<UsersLikeVideos> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(UsersLikeVideos::getUserId, userId)
                .eq(UsersLikeVideos::getVideoId, videoId);
        List<UsersLikeVideos> list = usersLikeVideosService.list(wrapper);
        return CollectionUtils.isNotEmpty(list);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void saveUserFanRelation(String userId, String fanId) {
        UsersFans userFan = new UsersFans();
        userFan.setUserId(userId);
        userFan.setFanId(fanId);

        usersFansService.save(userFan);

        this.getBaseMapper().addFansCount(userId);
        this.getBaseMapper().addFollersCount(fanId);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void deleteUserFanRelation(String userId, String fanId) {
        final LambdaUpdateWrapper<UsersFans> wrapper = Wrappers.lambdaUpdate();
        wrapper.eq(UsersFans::getUserId, userId)
                .eq(UsersFans::getFanId, fanId);
        //删除关系
        usersFansService.remove(wrapper);
        this.getBaseMapper().reduceFansCount(userId);
        this.getBaseMapper().reduceFollersCount(fanId);

    }

    @Override
    public boolean queryIfFollow(String userId, String fanId) {
        final LambdaQueryWrapper<UsersFans> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(UsersFans::getUserId, userId).eq(UsersFans::getFanId, fanId);
        List<UsersFans> list = usersFansService.list(wrapper);
        return CollectionUtils.isNotEmpty(list);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void reportUser(UsersReport userReport) {

        userReport.setCreateDate(LocalDateTime.now());
        usersReportService.save(userReport);
    }
}
