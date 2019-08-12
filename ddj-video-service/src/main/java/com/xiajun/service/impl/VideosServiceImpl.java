package com.xiajun.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiajun.mapper.*;
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
    private SearchRecordsMapper searchRecordsMapper;
    @Autowired
    private VideosMapper videosMapper;
    @Autowired
    private UsersLikeVideosMapper usersLikeVideosMapper;
    @Autowired
    private UsersMapper usersMapper;
    @Autowired
    private CommentsMapper commentsMapper;

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
            searchRecordsMapper.insert(record);
        }
        Page<VideosVO> videosVOPage = new Page<>();
        videosVOPage.setCurrent(page).setSize(pageSize);
        List<VideosVO> list = videosMapper.queryAllVideos(videosVOPage, desc, userId);

        videosVOPage.setRecords(list);

        return videosVOPage;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Page<VideosVO> queryMyLikeVideos(String userId, Integer page, Integer pageSize) {
        Page<VideosVO> videosVOPage = new Page<>();
        videosVOPage.setCurrent(page).setSize(pageSize);
        List<VideosVO> list = videosMapper.queryMyLikeVideos(userId);
        videosVOPage.setRecords(list);

        return videosVOPage;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Page<VideosVO> queryMyFollowVideos(String userId, Integer page, Integer pageSize) {
        Page<VideosVO> videosVOPage = new Page<>();
        videosVOPage.setCurrent(page).setSize(pageSize);

        List<VideosVO> list = videosMapper.queryMyFollowVideos(userId);

        videosVOPage.setRecords(list);
        return videosVOPage;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<String> getHotwords() {
        return searchRecordsMapper.getHotwords();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void userLikeVideo(String userId, String videoId, String videoCreaterId) {
        // 1. 保存用户和视频的喜欢点赞关联关系表
        UsersLikeVideos ulv = new UsersLikeVideos();

        ulv.setUserId(userId);
        ulv.setVideoId(videoId);
        usersLikeVideosMapper.insert(ulv);

        // 2. 视频喜欢数量累加
        videosMapper.addVideoLikeCount(videoId);

        // 3. 用户受喜欢数量的累加
        usersMapper.addReceiveLikeCount(videoCreaterId);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void userUnLikeVideo(String userId, String videoId, String videoCreaterId) {
        // 1. 删除用户和视频的喜欢点赞关联关系表
        QueryWrapper<UsersLikeVideos> wrapper = new QueryWrapper<>();

        wrapper.eq("user_id", userId).eq("video_id", videoId);


        usersLikeVideosMapper.delete(wrapper);

        // 2. 视频喜欢数量累减
        videosMapper.reduceVideoLikeCount(videoId);

        // 3. 用户受喜欢数量的累减
        usersMapper.reduceReceiveLikeCount(videoCreaterId);

    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void saveComment(Comments comment) {

        comment.setCreateTime(LocalDateTime.now());
        commentsMapper.insert(comment);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Page<CommentsVO> getAllComments(String videoId, Integer page, Integer pageSize) {


        Page<CommentsVO> commentsVOPage = new Page<>();
        commentsVOPage.setCurrent(page);
        commentsVOPage.setSize(pageSize);

        List<CommentsVO> list = commentsMapper.queryComments(commentsVOPage,videoId);

        for (CommentsVO c : list) {
            String timeAgo = TimeAgoUtils.format(c.getCreateTime());
            c.setTimeAgoStr(timeAgo);
        }

        commentsVOPage.setRecords(list);


        return commentsVOPage;
    }
}
