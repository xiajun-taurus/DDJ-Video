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
 * 用户粉丝关联关系表
 * </p>
 *
 * @author xiajun
 * @since 2019-05-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="UsersFans对象", description="用户粉丝关联关系表")
public class UsersFans implements Serializable {

private static final long serialVersionUID=1L;

    private String id;

    @ApiModelProperty(value = "用户")
    @TableField("user_id")
    private String userId;

    @ApiModelProperty(value = "粉丝")
    @TableField("fan_id")
    private String fanId;


}
