package com.novel.cloud.user.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

@Data
//@AllArgsConstructo
@Accessors(chain = true)
public class UserLoginVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long userId;

    private String nickname;

    // 用户头像
    private String avatar;

    private String token;
}
