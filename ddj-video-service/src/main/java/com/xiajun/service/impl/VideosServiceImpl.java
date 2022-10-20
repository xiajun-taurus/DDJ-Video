package com.xiajun.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiajun.mapper.VideosMapper;
import com.xiajun.pojo.Comments;
import com.xiajun.pojo.SearchRecords;
import com.xiajun.pojo.UsersLikeVideos;
import com.xiajun.pojo.Videos;
import com.xiajun.pojo.vo.CommentsVO;
import com.xiajun.pojo.vo.VideosVO;
import com.xiajun.service.IVideosService;
import com.xiajun.utils.TimeAgoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 视频信息表 服务实现类
 * </p>
 *
 * @author xiajun
 * @since 2019-05-25
 */
@Service
public class VideosServiceImpl extends ServiceImpl<VideosMapper, Videos> implements IVideosService {
    @Autowired
    private SearchRecordsServiceImpl searchRecordsService;
    @Autowired
    private VideosServiceImpl videosService;
    @Autowired
    private UsersLikeVideosServiceImpl usersLikeVideosService;
    @Autowired
    private UsersServiceImpl usersService;
    @Autowired
    private CommentsServiceImpl commentsService;

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public boolean save(Videos entity) {
        return super.save(entity);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public boolean updateById(Videos entity) {
        return super.updateById(entity);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Page<VideosVO> getAllVideos(Videos video, Integer isSaveRecord,
                                       Integer page, Integer pageSize) {

        // 保存热搜词
        String desc = video.getVideoDesc();
        String userId = video.getUserId();
        if (isSaveRecord != null && isSaveRecord == 1) {
            SearchRecords record = new SearchRecords();
            record.setContent(desc);
            searchRecordsService.save(record);
        }
        Page<VideosVO> videosVOPage = new Page<>();
        videosVOPage.setCurrent(page).setSize(pageSize);
        List<VideosVO> list = videosService.getBaseMapper().queryAllVideos(videosVOPage, desc, userId);

        videosVOPage.setRecords(list);

        return videosVOPage;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Page<VideosVO> queryMyLikeVideos(String userId, Integer page, Integer pageSize) {
        Page<VideosVO> videosVOPage = new Page<>();
        videosVOPage.setCurrent(page).setSize(pageSize);
        List<VideosVO> list = videosService.getBaseMapper().queryMyLikeVideos(userId);
        videosVOPage.setRecords(list);

        return videosVOPage;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Page<VideosVO> queryMyFollowVideos(String userId, Integer page, Integer pageSize) {
        Page<VideosVO> videosVOPage = new Page<>();
        videosVOPage.setCurrent(page).setSize(pageSize);

        List<VideosVO> list = videosService.getBaseMapper().queryMyFollowVideos(userId);

        videosVOPage.setRecords(list);
        return videosVOPage;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<String> getHotwords() {
        return searchRecordsService.getBaseMapper().getHotWords();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void userLikeVideo(String userId, String videoId, String videoCreaterId) {
        // 1. 保存用户和视频的喜欢点赞关联关系表
        UsersLikeVideos ulv = new UsersLikeVideos();

        ulv.setUserId(userId);
        ulv.setVideoId(videoId);
        usersLikeVideosService.save(ulv);

        // 2. 视频喜欢数量累加
        this.getBaseMapper().addVideoLikeCount(videoId);

        // 3. 用户受喜欢数量的累加
        usersService.getBaseMapper().addReceiveLikeCount(videoCreaterId);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void userUnLikeVideo(String userId, String videoId, String videoCreaterId) {
        // 1. 删除用户和视频的喜欢点赞关联关系表
        final LambdaUpdateWrapper<UsersLikeVideos> wrapper = Wrappers.lambdaUpdate();

        wrapper.eq(UsersLikeVideos::getUserId, userId)
                .eq(UsersLikeVideos::getVideoId, videoId);


        usersLikeVideosService.remove(wrapper);

        // 2. 视频喜欢数量累减
        this.getBaseMapper().reduceVideoLikeCount(videoId);

        // 3. 用户受喜欢数量的累减
        usersService.getBaseMapper().reduceReceiveLikeCount(videoCreaterId);

    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void saveComment(Comments comment) {

        comment.setCreateTime(LocalDateTime.now());
        commentsService.save(comment);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Page<CommentsVO> getAllComments(String videoId, Integer page, Integer pageSize) {


        Page<CommentsVO> commentsVOPage = new Page<>();
        commentsVOPage.setCurrent(page);
        commentsVOPage.setSize(pageSize);

        List<CommentsVO> list = commentsService.getBaseMapper().queryComments(commentsVOPage, videoId);

        for (CommentsVO c : list) {
            String timeAgo = TimeAgoUtils.format(c.getCreateTime());
            c.setTimeAgoStr(timeAgo);
        }

        commentsVOPage.setRecords(list);


        return commentsVOPage;
    }
}
