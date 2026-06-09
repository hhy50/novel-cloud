package com.ruoyi.framework.config;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.thymeleaf.dialect.SaTokenDialect;
import com.ruoyi.framework.config.properties.PermitAllUrlProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Sa-Token 配置类
 * 
 * @author ruoyi
 */
@Configuration
public class SaTokenConfig implements WebMvcConfigurer {
    
    @Autowired
    private PermitAllUrlProperties permitAllUrl;
    
    /**
     * 注册 Sa-Token 拦截器，打开注解式鉴权功能
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册 Sa-Token 拦截器，打开注解式鉴权功能
        registry.addInterceptor(new SaInterceptor(handle -> {
            // 登录校验 -- 拦截所有路由
            SaRouter.match("/**")
                // 放行的路由
                .notMatch("/login", "/register", "/captcha/**")
                .notMatch("/favicon.ico", "/ruoyi.png")
                .notMatch("/html/**", "/css/**", "/docs/**")
                .notMatch("/fonts/**", "/img/**", "/ajax/**")
                .notMatch("/js/**", "/ruoyi/**")
                .notMatch(permitAllUrl.getUrls())
                // 检查登录
                .check(r -> StpUtil.checkLogin());
        })).addPathPatterns("/**");
    }
    
    /**
     * Sa-Token 整合 Thymeleaf 方言
     */
    @Bean
    public SaTokenDialect getSaTokenDialect() {
        return new SaTokenDialect();
    }
}
