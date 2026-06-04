package com.novel.user.dto;

import java.io.Serial;
import java.io.Serializable;
import lombok.Data;

/**
 * User profile response DTO
 */
@Data
public class UserProfileVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long userId;
    private String nickname;
    private String avatar;
    private Boolean vip;
    private Long coins;
    private Long points;
}
