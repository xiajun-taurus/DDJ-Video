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
import java.time.LocalDateTime;

/**
 * <p>
 * 视频信息表
 * </p>
 *
 * @author xiajun
 * @since 2019-05-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "Videos对象", description = "视频信息表")
public class Videos implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.UUID)
    private String id;

    @ApiModelProperty(value = "发布者id")
    @TableField("user_id")
    private String userId;

    @ApiModelProperty(value = "用户使用音频的信息")
    @TableField("audio_id")
    private String audioId;

    @ApiModelProperty(value = "视频描述")
    @TableField("video_desc")
    private String videoDesc;

    @ApiModelProperty(value = "视频存放的路径")
    @TableField("video_path")
    private String videoPath;

    @ApiModelProperty(value = "视频秒数")
    @TableField("video_seconds")
    private Float videoSeconds;

    @ApiModelProperty(value = "视频宽度")
    @TableField("video_width")
    private Integer videoWidth;

    @ApiModelProperty(value = "视频高度")
    @TableField("video_height")
    private Integer videoHeight;

    @ApiModelProperty(value = "视频封面图")
    @TableField("cover_path")
    private String coverPath;

    @ApiModelProperty(value = "喜欢/赞美的数量")
    @TableField("like_counts")
    private Long likeCounts;

    @ApiModelProperty(value = "视频状态： 1、发布成功 2、禁止播放，管理员操作")
    @TableField("status")
    private Integer status;

    @ApiModelProperty(value = "创建时间")
    @TableField("create_time")
    private LocalDateTime createTime;
}
