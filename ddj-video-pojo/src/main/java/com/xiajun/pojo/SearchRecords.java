package com.xiajun.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 视频搜索的记录表
 * </p>
 *
 * @author xiajun
 * @since 2019-05-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="SearchRecords对象", description="视频搜索的记录表")
public class SearchRecords implements Serializable {

private static final long serialVersionUID=1L;

    private String id;

    @ApiModelProperty(value = "搜索的内容")
    @TableField("content")
    private String content;


}
