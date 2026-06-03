package com.novel.user.adapter.web;

import com.novel.common.core.domain.R;
import com.novel.user.app.UserAppService;
import com.novel.user.dto.UserLoginDto;
import com.novel.user.dto.UserLoginVo;
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

    private final UserAppService userAppService;

    @PostMapping("/login")
    public Mono<R<UserLoginVo>> login(@Valid @RequestBody UserLoginDto params) {
        return userAppService.login(params).map(R::ok);
    }
}
