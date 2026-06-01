package com.novel.user.api;

import com.novel.common.core.domain.R;
import com.novel.user.dto.UserInfoVo;
import com.novel.user.dto.UserLoginDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "novel-user-server", contextId = "userOpenFeignApi", path = "/api/open/user")
public interface UserOpenFeignApi {

    @PostMapping("/login")
    R<UserInfoVo> login(@RequestBody UserLoginDto params);
}
