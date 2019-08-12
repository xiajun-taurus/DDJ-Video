package com.xiajun.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiajun.mapper.UsersFansMapper;
import com.xiajun.mapper.UsersLikeVideosMapper;
import com.xiajun.mapper.UsersMapper;
import com.xiajun.mapper.UsersReportMapper;
import com.xiajun.pojo.Users;
import com.xiajun.pojo.UsersFans;
import com.xiajun.pojo.UsersLikeVideos;
import com.xiajun.pojo.UsersReport;
import com.xiajun.service.IUsersService;
import com.xiajun.utils.MD5Utils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

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
    private UsersMapper usersMapper;
    @Autowired
    private UsersLikeVideosMapper usersLikeVideosMapper;
    @Autowired
    private UsersFansMapper usersFansMapper;
    @Autowired
    private UsersReportMapper usersReportMapper;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public boolean queryUserIsExist(String username) {
        QueryWrapper<Users> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username);
        return usersMapper.selectOne(wrapper) != null;
    }

    @Override
    public Users queryUserForLogin(String username, String password) throws Exception {
        QueryWrapper<Users> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username)
                .eq("password", MD5Utils.getMD5Str(password));
        return usersMapper.selectOne(wrapper);
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

        QueryWrapper<UsersLikeVideos> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId).eq("video_id", videoId);
        List<UsersLikeVideos> list = usersLikeVideosMapper.selectList(wrapper);
        if (list != null && list.size() > 0) {
            return true;
        }

        return false;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void saveUserFanRelation(String userId, String fanId) {
        UsersFans userFan = new UsersFans();
        userFan.setUserId(userId);
        userFan.setFanId(fanId);

        usersFansMapper.insert(userFan);

        usersMapper.addFansCount(userId);
        usersMapper.addFollersCount(fanId);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void deleteUserFanRelation(String userId, String fanId) {
        QueryWrapper<UsersFans> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId).eq("fan_id", fanId);
        //删除关系
        usersFansMapper.delete(wrapper);
        usersMapper.reduceFansCount(userId);
        usersMapper.reduceFollersCount(fanId);

    }

    @Override
    public boolean queryIfFollow(String userId, String fanId) {
        QueryWrapper<UsersFans> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId).eq("fan_id", fanId);
        List<UsersFans> list = usersFansMapper.selectList(wrapper);

        if (list != null && !list.isEmpty() && list.size() > 0) {
            return true;
        }
        return false;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void reportUser(UsersReport userReport) {

        userReport.setCreateDate(LocalDateTime.now());
        usersReportMapper.insert(userReport);
    }
}
