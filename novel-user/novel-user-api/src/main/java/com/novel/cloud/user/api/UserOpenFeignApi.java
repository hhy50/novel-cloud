package com.novel.cloud.user.api;

import com.novel.cloud.common.domain.R;
import com.novel.cloud.user.dto.UserRewardAddDto;
import com.novel.cloud.user.dto.UserRewardAddVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 用户服务 OpenFeign 接口。
 */
@FeignClient(name = "novel-user-service", contextId = "userOpenFeignApi", path = "/api/open/user")
public interface UserOpenFeignApi {

    /**
     * 增加用户奖励。
     */
    @PostMapping("/reward/add")
    R<UserRewardAddVo> addReward(@RequestBody UserRewardAddDto dto);
}
