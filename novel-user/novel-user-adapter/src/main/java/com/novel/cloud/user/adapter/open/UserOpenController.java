package com.novel.cloud.user.adapter.open;

import com.novel.cloud.common.domain.R;
import com.novel.cloud.user.app.WalletAppService;
import com.novel.cloud.user.dto.UserRewardAddDto;
import com.novel.cloud.user.dto.UserRewardAddVo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户 Open API Controller（服务间调用接口）。
 */
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/open/user")
public class UserOpenController {

    private final WalletAppService walletAppService;

    @PostMapping("/reward/add")
    public R<UserRewardAddVo> addReward(@Valid @RequestBody UserRewardAddDto dto) {
        return R.ok(walletAppService.addReward(dto));
    }
}
