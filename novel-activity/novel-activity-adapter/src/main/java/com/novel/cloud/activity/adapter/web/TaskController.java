package com.novel.cloud.activity.adapter.web;

import com.novel.cloud.activity.app.TaskAppService;
import com.novel.cloud.activity.dto.ClaimTaskRewardDto;
import com.novel.cloud.activity.dto.ClaimTaskRewardVo;
import com.novel.cloud.activity.dto.DailyTasksVo;
import com.novel.cloud.common.domain.R;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 每日任务控制器。
 */
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/activity/tasks")
public class TaskController {

    private final TaskAppService taskAppService;

    @GetMapping("/daily")
    public R<DailyTasksVo> getDailyTasks() {
        return R.ok(taskAppService.getDailyTasks());
    }

    @PostMapping("/claim")
    public R<ClaimTaskRewardVo> claimTaskReward(@Valid @RequestBody ClaimTaskRewardDto params) {
        return R.ok(taskAppService.claimTaskReward(params));
    }
}
