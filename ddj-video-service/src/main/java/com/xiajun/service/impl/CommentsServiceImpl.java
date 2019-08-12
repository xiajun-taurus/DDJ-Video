package com.xiajun.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiajun.mapper.CommentsMapper;
import com.xiajun.pojo.Comments;
import com.xiajun.service.ICommentsService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 课程评论表 服务实现类
 * </p>
 *
 * @author xiajun
 * @since 2019-05-25
 */
@Service
public class CommentsServiceImpl extends ServiceImpl<CommentsMapper, Comments> implements ICommentsService {

}