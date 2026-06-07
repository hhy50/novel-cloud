package com.novel.cloud.activity.adapter.web;

import cn.dev33.satoken.stp.SaLoginConfig;
import cn.dev33.satoken.stp.StpUtil;
import com.novel.cloud.common.domain.R;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 测试控制器 - 仅用于开发测试
 */
@RestController
@RequestMapping("/api/activity/test")
@RequiredArgsConstructor
public class TestController {

    /**
     * 模拟登录 - 获取token
     */
    @PostMapping("/login")
    public R<Map<String, Object>> login(@RequestBody LoginRequest request) {
        try {
            Long userId = request.getUserId() != null ? request.getUserId() : 10001L;

            // Sa-Token登录（在Servlet环境下正常工作）
            StpUtil.login(userId, SaLoginConfig
                    .setExtra("userId", userId)
                    .setExtra("nickname", "测试用户" + userId));

            Map<String, Object> data = new HashMap<>();
            data.put("token", StpUtil.getTokenValue());
            data.put("tokenName", StpUtil.getTokenName());
            data.put("userId", userId);
            data.put("message", "登录成功，请在请求头中添加 " + StpUtil.getTokenName() + ": " + StpUtil.getTokenValue());

            return R.ok(data);
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail("登录失败: " + e.getMessage());
        }
    }

    /**
     * 检查登录状态
     */
    @GetMapping("/check")
    public R<Map<String, Object>> check() {
        Map<String, Object> data = new HashMap<>();
        data.put("isLogin", StpUtil.isLogin());
        if (StpUtil.isLogin()) {
            data.put("userId", StpUtil.getLoginId());
            data.put("token", StpUtil.getTokenValue());
        }
        return R.ok(data);
    }

    @Data
    public static class LoginRequest {
        private Long userId;
    }
}
