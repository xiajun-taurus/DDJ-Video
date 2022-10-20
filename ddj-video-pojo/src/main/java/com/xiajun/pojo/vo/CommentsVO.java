package com.xiajun.pojo.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 课程评论表
 * </p>
 *
 * @author xiajun
 * @since 2019-05-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="Comments对象", description="课程评论表")
public class CommentsVO implements Serializable {

private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    @TableField("father_comment_id")
    private String fatherCommentId;

    @TableField("to_user_id")
    private String toUserId;

    @ApiModelProperty(value = "视频id")
    @TableField("video_id")
    private String videoId;

    @ApiModelProperty(value = "留言者，评论的用户id")
    @TableField("from_user_id")
    private String fromUserId;

    @ApiModelProperty(value = "评论内容")
    @TableField("comment")
    private String comment;

    @TableField("create_time")
    private Date createTime;

    private String faceImage;
    private String nickname;
    private String toNickname;
    private String timeAgoStr;
}
