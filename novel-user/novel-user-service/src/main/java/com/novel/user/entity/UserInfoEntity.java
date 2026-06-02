package com.novel.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import lombok.Data;

@Data
@TableName("t_user_info")
public class UserInfoEntity {

    @TableId(type = IdType.INPUT)
    private Long id;

    private String nickname;

    private String avatar;

    private Integer vipStatus;

    private String deviceId;

    private String deviceName;

    private String osType;

    private String appVersion;

    private String country;

    private String language;

    private String ip;

    private String token;

    private LocalDateTime lastLoginTime;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
