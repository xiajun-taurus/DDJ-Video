package com.xiajun.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiajun.pojo.SearchRecords;

import java.util.List;

/**
 * <p>
 * 视频搜索的记录表 Mapper 接口
 * </p>
 *
 * @author xiajun
 * @since 2019-05-25
 */
public interface SearchRecordsMapper extends BaseMapper<SearchRecords> {
    List<String> getHotWords();
}
