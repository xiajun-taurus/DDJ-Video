package com.xiajun.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiajun.mapper.SearchRecordsMapper;
import com.xiajun.pojo.SearchRecords;
import com.xiajun.service.ISearchRecordsService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 视频搜索的记录表 服务实现类
 * </p>
 *
 * @author xiajun
 * @since 2019-05-25
 */
@Service
public class SearchRecordsServiceImpl extends ServiceImpl<SearchRecordsMapper, SearchRecords> implements ISearchRecordsService {

}
