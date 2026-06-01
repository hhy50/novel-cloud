package com.novel.user.service.impl;

import com.novel.user.dto.UserInfoVo;
import com.novel.user.dto.UserLoginDto;
import com.novel.user.service.UserService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UserServiceImpl implements UserService {

    @Override
    public Mono<UserInfoVo> login(UserLoginDto params) {
        UserInfoVo userInfoVo = new UserInfoVo();
        userInfoVo.setUserId(1L);
        userInfoVo.setUsername(params.getUsername());
        userInfoVo.setNickname("演示用户");
        userInfoVo.setAvatar("https://static.example.com/avatar/default.png");
        userInfoVo.setVip(Boolean.TRUE);
        return Mono.just(userInfoVo);
    }
}
