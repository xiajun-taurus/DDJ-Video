package com.xiajun.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiajun.pojo.Comments;
import com.xiajun.pojo.vo.CommentsVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 课程评论表 Mapper 接口
 * </p>
 *
 * @author xiajun
 * @since 2019-05-25
 */
public interface CommentsMapper extends BaseMapper<Comments> {
    List<CommentsVO> queryComments(Page<CommentsVO> page,@Param("videoId") String videoId);
}
