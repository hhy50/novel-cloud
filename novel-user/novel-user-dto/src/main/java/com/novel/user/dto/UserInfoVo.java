package com.novel.user.dto;

import java.io.Serial;
import java.io.Serializable;

import lombok.Data;

@Data
public class UserInfoVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long userId;

    private String guestId;

    private String nickname;

    private String avatar;

    private Boolean vip;

    private String deviceId;

    private String region;

    private String ip;
}
