package com.novel.user.controller;

import com.novel.common.core.domain.R;
import com.novel.user.dto.UserLoginDto;
import com.novel.user.dto.UserLoginVo;
import com.novel.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @PostMapping("/login")
    public Mono<R<UserLoginVo>> login(@Valid @RequestBody UserLoginDto params) {
        return userService.login(params).map(R::ok);
    }
}
