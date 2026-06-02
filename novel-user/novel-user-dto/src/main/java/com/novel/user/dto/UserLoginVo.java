package com.novel.user.dto;

import java.io.Serial;
import java.io.Serializable;

import lombok.Data;

@Data
public class UserLoginVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String token;

    private String tokenName;

    private Long expireSeconds;

    private UserInfoVo userInfo;
}
