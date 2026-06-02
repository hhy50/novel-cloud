package com.novel.user.dto;

import java.io.Serial;
import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
//@AllArgsConstructo
@Accessors(chain = true)
public class UserLoginVo implements Serializable {

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

    private String token;
}
