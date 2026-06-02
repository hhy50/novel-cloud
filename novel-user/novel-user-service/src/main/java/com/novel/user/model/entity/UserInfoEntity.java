package com.novel.user.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import lombok.Data;

@Data
@TableName("user_info")
public class UserInfoEntity {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String guestId;

    private String nickname;

    private String avatar;

    private Integer vipStatus;

    private String deviceId;

    private String deviceName;

    private String osType;

    private String appVersion;

    private String region;

    private String ip;

    private LocalDateTime lastLoginTime;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
