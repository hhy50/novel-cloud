package com.novel.user.service;

import com.novel.user.dto.UserInfoVo;
import com.novel.user.dto.UserLoginDto;
import reactor.core.publisher.Mono;

public interface UserService {

    Mono<UserInfoVo> login(UserLoginDto params);
}
