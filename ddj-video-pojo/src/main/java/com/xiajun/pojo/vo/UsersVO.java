package com.xiajun.pojo.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UsersVO {
    @ApiModelProperty(hidden = true)
    private String id;

    @ApiModelProperty(value = "用户名",name = "username",example = "zhangsan",required = true)
    private String username;

    @ApiModelProperty(value = "密码",name="password",example = "123456",required = true)
    @JsonIgnore
    private String password;

    @ApiModelProperty(value = "我的头像，如果没有默认给一张",hidden = true)
    private String faceImage;

    @ApiModelProperty(value = "昵称",hidden = true)
    private String nickname;

    @ApiModelProperty(value = "我的粉丝数量",hidden = true)
    private Integer fansCounts;

    @ApiModelProperty(value = "我关注的人总数" ,hidden = true)
    private Integer followCounts;

    @ApiModelProperty(value = "我接受到的赞美/收藏 的数量",hidden = true)
    private Integer receiveLikeCounts;

    private String userToken;
    private boolean isFollow;
}
