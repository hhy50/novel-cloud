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
