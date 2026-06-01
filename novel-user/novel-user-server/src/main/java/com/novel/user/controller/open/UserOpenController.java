package com.novel.user.controller.open;

import com.novel.common.core.domain.R;
import com.novel.user.api.UserOpenFeignApi;
import com.novel.user.dto.UserInfoVo;
import com.novel.user.dto.UserLoginDto;
import com.novel.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequiredArgsConstructor
public class UserOpenController implements UserOpenFeignApi {

    private final UserService userService;

    @Override
    public R<UserInfoVo> login(@Valid @RequestBody UserLoginDto params) {
        return R.ok(userService.login(params).block());
    }
}
