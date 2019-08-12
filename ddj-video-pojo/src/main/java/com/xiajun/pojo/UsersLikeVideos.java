package com.xiajun.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户喜欢的/赞过的视频
 * </p>
 *
 * @author xiajun
 * @since 2019-05-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="UsersLikeVideos对象", description="用户喜欢的/赞过的视频")
public class UsersLikeVideos implements Serializable {

private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.UUID)
    private String id;

    @ApiModelProperty(value = "用户")
    @TableField("user_id")
    private String userId;

    @ApiModelProperty(value = "视频")
    @TableField("video_id")
    private String videoId;


}
