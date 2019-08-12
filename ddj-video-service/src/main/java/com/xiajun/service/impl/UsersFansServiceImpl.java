package com.xiajun.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiajun.mapper.UsersFansMapper;
import com.xiajun.pojo.UsersFans;
import com.xiajun.service.IUsersFansService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户粉丝关联关系表 服务实现类
 * </p>
 *
 * @author xiajun
 * @since 2019-05-25
 */
@Service
public class UsersFansServiceImpl extends ServiceImpl<UsersFansMapper, UsersFans> implements IUsersFansService {

}
