package com.novel.cloud.config.webflux;

import cn.dev33.satoken.reactor.filter.SaReactorFilter;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SaTokenConfigure {

    @Bean
    public SaReactorFilter getSaReactorFilter() {
        return new SaReactorFilter()
                // 指定拦截路由
                .addInclude("/**")
                // 指定放行路由
                .addExclude("/favicon.ico", "/api/user/login")
                // 认证函数
                .setAuth(obj -> {
                    SaRouter.match("/**").check(r -> StpUtil.checkLogin());
                })
                // 异常处理
                .setError(e -> SaResult.error(e.getMessage()));
    }
}
