package com.novel.cloud.config.webmvc;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.filter.SaServletFilter;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import com.novel.cloud.common.domain.R;
import com.novel.cloud.common.util.JSONUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.novel.cloud.consts.BizErrType.INVALID_TOKEN;

@Configuration
public class SaTokenConfigure {

    @Bean
    public SaServletFilter getSaServletFilter() {
        return new SaServletFilter()
                // 指定拦截路由
                .addInclude("/**")
                // 指定放行路由（admin 路径由独立 admin 鉴权接管，C 端登录态不参与）
                .addExclude("/favicon.ico", "/api/user/login", "/api/open/**", "/api/activity/admin/**")
                // 认证函数
                .setAuth(obj -> {
                    SaRouter.match("/**").check(r -> StpUtil.checkLogin());
                })
                // 异常处理
                .setError(e -> {
                    SaHolder.getResponse().setHeader("Content-Type", "application/json;charset=UTF-8");
                    return JSONUtil.toJSONString(R.fail(INVALID_TOKEN));
                });
    }
}
