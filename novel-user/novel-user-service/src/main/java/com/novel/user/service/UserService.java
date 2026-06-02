package com.novel.user.service;

import com.novel.user.dto.UserLoginDto;
import com.novel.user.dto.UserLoginVo;
import reactor.core.publisher.Mono;

public interface UserService {

    Mono<UserLoginVo> login(UserLoginDto params);
}
