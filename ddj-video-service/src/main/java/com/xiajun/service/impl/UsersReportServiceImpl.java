package com.xiajun.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiajun.mapper.UsersReportMapper;
import com.xiajun.pojo.UsersReport;
import com.xiajun.service.IUsersReportService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 举报用户表 服务实现类
 * </p>
 *
 * @author xiajun
 * @since 2019-05-25
 */
@Service
public class UsersReportServiceImpl extends ServiceImpl<UsersReportMapper, UsersReport> implements IUsersReportService {

}
