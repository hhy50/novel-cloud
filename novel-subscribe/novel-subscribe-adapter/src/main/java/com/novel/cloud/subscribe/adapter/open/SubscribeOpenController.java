package com.novel.cloud.subscribe.adapter.open;

import com.novel.cloud.common.domain.R;
import com.novel.cloud.subscribe.app.SubscribeAppService;
import com.novel.cloud.subscribe.dto.SubscribeActivateDto;
import com.novel.cloud.subscribe.dto.UserSubscribeVo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * 订阅 Open API Controller（服务间调用接口）
 * Adapter层：供支付模块回调使用
 */
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/open/subscribe")
public class SubscribeOpenController {

    private final SubscribeAppService subscribeAppService;

    @PostMapping("/activate")
    public Mono<R<UserSubscribeVo>> activateSubscribe(@Valid @RequestBody SubscribeActivateDto dto) {
        return subscribeAppService.activateSubscribe(dto).map(R::ok);
    }
}
