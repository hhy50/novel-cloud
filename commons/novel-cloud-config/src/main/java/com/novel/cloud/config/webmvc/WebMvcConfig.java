package com.novel.cloud.config.webmvc;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Spring MVC 配置
 * 注册 TraceId 过滤器、请求日志拦截器、请求元数据拦截器
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    /** TraceId 过滤器：最高优先级，保证所有日志都带 traceId */
    @Bean
    public FilterRegistrationBean<TraceIdFilter> traceIdFilterRegistration() {
        FilterRegistrationBean<TraceIdFilter> bean = new FilterRegistrationBean<>(new TraceIdFilter());
        bean.addUrlPatterns("/*");
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return bean;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 请求日志（接口请求 + 耗时），仅对 @RequestMapping 类方法生效
        registry.addInterceptor(new RequestLogInterceptor())
                .order(0)
                .addPathPatterns("/**")
                .excludePathPatterns("/static/**", "/favicon.ico");

        // 请求元数据（提取请求头到上下文）
        registry.addInterceptor(new RequestMetadataInterceptor())
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns("/static/**", "/favicon.ico");
    }
}
