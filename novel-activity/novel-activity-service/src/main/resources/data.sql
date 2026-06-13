-- ========================================
-- 活动服务 初始化数据 (H2内存数据库)
-- ========================================

-- 插入默认签到配置
INSERT INTO activity_checkin_config (
    config_name, config_code,
    monday_reward, tuesday_reward, wednesday_reward, thursday_reward,
    friday_reward, saturday_reward, sunday_reward,
    daily_reward_default,
    continuous_bonus_json,
    start_time, end_time, status
) VALUES (
    '默认签到配置', 'DEFAULT',
    10, 10, 10, 10, 10, 15, 15,
    10,
    '[{"days":3,"bonus":30,"name":"连续3天奖励"},{"days":5,"bonus":50,"name":"连续5天奖励"},{"days":7,"bonus":100,"name":"完美周奖励"}]',
    '2024-01-01 00:00:00', NULL, 1
);

-- 插入默认每日任务配置
INSERT INTO t_daily_task (
    task_code, task_name, task_desc,
    reward_coins, reward_points, target_count, sort_order, status
) VALUES
    ('READ', '每日阅读', '完成每日阅读任务', 10, 10, 1, 1, 1),
    ('CHECKIN', '每日签到', '完成每日签到任务', 5, 5, 1, 2, 1);
