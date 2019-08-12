package com.xiajun.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

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
public class VideosVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    @ApiModelProperty(value = "发布者id")
    private String userId;

    @ApiModelProperty(value = "用户使用音频的信息")
    private String audioId;

    @ApiModelProperty(value = "视频描述")
    private String videoDesc;

    @ApiModelProperty(value = "视频存放的路径")
    private String videoPath;

    @ApiModelProperty(value = "视频秒数")
    private Float videoSeconds;

    @ApiModelProperty(value = "视频宽度")
    private Integer videoWidth;

    @ApiModelProperty(value = "视频高度")
    private Integer videoHeight;

    @ApiModelProperty(value = "视频封面图")
    private String coverPath;

    @ApiModelProperty(value = "喜欢/赞美的数量")
    private Long likeCounts;

    @ApiModelProperty(value = "视频状态： 1、发布成功 2、禁止播放，管理员操作")
    private Integer status;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    private String faceImage;
    private String nickname;
}
