package com.xiajun.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
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
@ApiModel(value="Users对象", description="这是用户对象")
public class Users implements Serializable {

private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.UUID)
    @ApiModelProperty(hidden = true)
    private String id;

    @ApiModelProperty(value = "用户名",name = "username",example = "zhangsan",required = true)
    @TableField("username")
    private String username;

    @ApiModelProperty(value = "密码",name="password",example = "123456",required = true)
    @TableField("password")
    private String password;

    @ApiModelProperty(value = "我的头像，如果没有默认给一张",hidden = true)
    @TableField("face_image")
    private String faceImage;

    @ApiModelProperty(value = "昵称",hidden = true)
    @TableField("nickname")
    private String nickname;

    @ApiModelProperty(value = "我的粉丝数量",hidden = true)
    @TableField("fans_counts")
    private Integer fansCounts;

    @ApiModelProperty(value = "我关注的人总数" ,hidden = true)
    @TableField("follow_counts")
    private Integer followCounts;

    @ApiModelProperty(value = "我接受到的赞美/收藏 的数量",hidden = true)
    @TableField("receive_like_counts")
    private Integer receiveLikeCounts;


}
