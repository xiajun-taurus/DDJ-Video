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
 * 
 * </p>
 *
 * @author xiajun
 * @since 2019-05-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="Bgm对象", description="")
public class Bgm implements Serializable {

private static final long serialVersionUID=1L;

    private String id;

    @TableField("author")
    private String author;

    @TableField("name")
    private String name;

    @ApiModelProperty(value = "播放地址")
    @TableField("path")
    private String path;


}
