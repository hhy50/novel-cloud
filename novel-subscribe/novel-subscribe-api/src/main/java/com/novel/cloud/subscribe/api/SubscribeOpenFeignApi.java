package com.novel.cloud.subscribe.api;

import com.novel.cloud.common.domain.R;
import com.novel.cloud.subscribe.dto.SubscribeActivateDto;
import com.novel.cloud.subscribe.dto.UserSubscribeVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 订阅服务 OpenFeign 接口
 * 供支付模块回调激活订阅使用
 */
@FeignClient(name = "novel-subscribe-service", contextId = "subscribeOpenFeignApi", path = "/api/open/subscribe")
public interface SubscribeOpenFeignApi {

    /**
     * 支付成功后激活订阅
     */
    @PostMapping("/activate")
    R<UserSubscribeVo> activateSubscribe(@RequestBody SubscribeActivateDto dto);
}
