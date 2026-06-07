package com.novel.cloud.activity.api;

import com.novel.cloud.common.domain.R;
import com.novel.cloud.activity.dto.CheckinResultVo;
import com.novel.cloud.activity.dto.CheckinStatusVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * 活动服务OpenFeign接口
 */
@FeignClient(name = "novel-activity", contextId = "activityOpenFeignApi")
public interface ActivityOpenFeignApi {

    /**
     * 获取签到状态（内部调用）
     */
    @GetMapping("/api/open/activity/checkin/status")
    R<CheckinStatusVo> getCheckinStatus();

    /**
     * 执行签到（内部调用）
     */
    @PostMapping("/api/open/activity/checkin/perform")
    R<CheckinResultVo> performCheckin();
}
