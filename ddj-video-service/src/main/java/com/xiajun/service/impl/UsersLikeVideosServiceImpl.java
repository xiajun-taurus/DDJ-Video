package com.xiajun.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiajun.mapper.UsersLikeVideosMapper;
import com.xiajun.pojo.UsersLikeVideos;
import com.xiajun.service.IUsersLikeVideosService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户喜欢的/赞过的视频 服务实现类
 * </p>
 *
 * @author xiajun
 * @since 2019-05-25
 */
@Service
public class UsersLikeVideosServiceImpl extends ServiceImpl<UsersLikeVideosMapper, UsersLikeVideos> implements IUsersLikeVideosService {

}
