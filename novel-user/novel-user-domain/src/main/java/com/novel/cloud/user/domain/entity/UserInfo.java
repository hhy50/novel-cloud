package com.novel.cloud.user.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户信息领域实体
 */
@Data
public class UserInfo {

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
