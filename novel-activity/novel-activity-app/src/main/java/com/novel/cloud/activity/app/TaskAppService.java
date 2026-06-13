package com.novel.cloud.activity.app;

import cn.dev33.satoken.stp.StpUtil;
import com.novel.cloud.activity.domain.entity.DailyTask;
import com.novel.cloud.activity.domain.entity.UserTaskRecord;
import com.novel.cloud.activity.domain.repository.DailyTaskRepository;
import com.novel.cloud.activity.domain.repository.UserCoinRepository;
import com.novel.cloud.activity.domain.repository.UserTaskRecordRepository;
import com.novel.cloud.activity.dto.ClaimTaskRewardDto;
import com.novel.cloud.activity.dto.ClaimTaskRewardVo;
import com.novel.cloud.activity.dto.DailyTaskVo;
import com.novel.cloud.activity.dto.DailyTasksVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 每日任务应用服务。
 */
@Service
@RequiredArgsConstructor
public class TaskAppService {

    private final DailyTaskRepository dailyTaskRepository;
    private final UserTaskRecordRepository userTaskRecordRepository;
    private final UserCoinRepository userCoinRepository;

    public DailyTasksVo getDailyTasks() {
        Long userId = StpUtil.getLoginIdAsLong();
        LocalDate today = LocalDate.now();

        List<DailyTask> tasks = dailyTaskRepository.findAllActive();
        List<UserTaskRecord> records = userTaskRecordRepository.findByUserIdAndDate(userId, today);

        Map<Long, UserTaskRecord> recordMap = records.stream()
                .collect(Collectors.toMap(UserTaskRecord::getTaskId, r -> r));

        List<DailyTaskVo> taskVos = tasks.stream()
                .map(task -> {
                    UserTaskRecord record = recordMap.get(task.getId());
                    DailyTaskVo vo = new DailyTaskVo();
                    vo.setTaskId(task.getId());
                    vo.setTaskCode(task.getTaskCode());
                    vo.setTaskName(task.getTaskName());
                    vo.setTaskDesc(task.getTaskDesc());
                    vo.setRewardCoins(task.getRewardCoins());
                    vo.setRewardPoints(task.getRewardPoints());
                    vo.setTargetCount(task.getTargetCount());

                    if (record != null) {
                        vo.setCurrentCount(record.getCurrentCount());
                        vo.setCompleted(record.getCompleted());
                        vo.setRewardClaimed(record.getRewardClaimed());
                    } else {
                        vo.setCurrentCount(0);
                        vo.setCompleted(false);
                        vo.setRewardClaimed(false);
                    }
                    return vo;
                })
                .collect(Collectors.toList());

        int completedCount = (int) taskVos.stream().filter(DailyTaskVo::getRewardClaimed).count();

        DailyTasksVo result = new DailyTasksVo();
        result.setTasks(taskVos);
        result.setCompletedCount(completedCount);
        result.setTotalCount(tasks.size());
        return result;
    }

    @Transactional
    public ClaimTaskRewardVo claimTaskReward(ClaimTaskRewardDto params) {
        Long userId = StpUtil.getLoginIdAsLong();
        LocalDate today = LocalDate.now();

        DailyTask task = dailyTaskRepository.findById(params.getTaskId());
        if (task == null) {
            ClaimTaskRewardVo vo = new ClaimTaskRewardVo();
            vo.setSuccess(false);
            vo.setMessage("Task not found");
            return vo;
        }

        UserTaskRecord record = userTaskRecordRepository.findByUserIdAndTaskIdAndDate(
                userId, params.getTaskId(), today);

        if (record == null || !record.getCompleted()) {
            ClaimTaskRewardVo vo = new ClaimTaskRewardVo();
            vo.setSuccess(false);
            vo.setMessage("Task not completed");
            return vo;
        }

        if (record.getRewardClaimed()) {
            ClaimTaskRewardVo vo = new ClaimTaskRewardVo();
            vo.setSuccess(false);
            vo.setMessage("Reward already claimed");
            return vo;
        }

        userCoinRepository.addReward(userId, task.getRewardCoins(), task.getRewardPoints(), "TASK", "Daily task: " + task.getTaskName());

        record.setRewardClaimed(true);
        userTaskRecordRepository.updateById(record);

        ClaimTaskRewardVo vo = new ClaimTaskRewardVo();
        vo.setSuccess(true);
        vo.setRewardCoins(task.getRewardCoins());
        vo.setRewardPoints(task.getRewardPoints());
        vo.setMessage("Reward claimed successfully!");
        return vo;
    }
}
