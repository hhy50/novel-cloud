-- ========================================
-- 活动服务 Schema (H2内存数据库)
-- ========================================

-- 签到配置表
CREATE TABLE IF NOT EXISTS activity_checkin_config (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    config_name VARCHAR(100) NOT NULL,
    config_code VARCHAR(50) NOT NULL UNIQUE,
    monday_reward INT DEFAULT 10,
    tuesday_reward INT DEFAULT 10,
    wednesday_reward INT DEFAULT 10,
    thursday_reward INT DEFAULT 10,
    friday_reward INT DEFAULT 10,
    saturday_reward INT DEFAULT 15,
    sunday_reward INT DEFAULT 15,
    daily_reward_default INT DEFAULT 10,
    continuous_bonus_json TEXT,
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NULL,
    status TINYINT DEFAULT 1,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 用户签到记录表
CREATE TABLE IF NOT EXISTS activity_user_checkin_record (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    checkin_date DATE NOT NULL,
    checkin_time TIMESTAMP NOT NULL,
    week_of_year INT NOT NULL,
    day_of_week TINYINT NOT NULL,
    daily_reward INT DEFAULT 0,
    total_bonus INT DEFAULT 0,
    total_coins INT NOT NULL,
    continuous_days INT DEFAULT 0,
    bonus_details_json TEXT,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_user_date (user_id, checkin_date)
);

-- 用户周签到状态表
CREATE TABLE IF NOT EXISTS activity_user_checkin_week_status (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    week_of_year INT NOT NULL,
    checkin_days INT DEFAULT 0,
    checkin_count INT DEFAULT 0,
    continuous_days INT DEFAULT 0,
    claimed_bonus_mask INT DEFAULT 0,
    week_start_date DATE NOT NULL,
    week_end_date DATE NOT NULL,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_user_week (user_id, week_of_year)
);

-- 每日任务配置表
CREATE TABLE IF NOT EXISTS t_daily_task (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    task_code VARCHAR(64) NOT NULL,
    task_name VARCHAR(100) NOT NULL,
    task_desc VARCHAR(255),
    reward_coins INT DEFAULT 0,
    reward_points INT DEFAULT 0,
    target_count INT DEFAULT 1,
    sort_order INT DEFAULT 0,
    status TINYINT DEFAULT 1,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 用户每日任务记录表
CREATE TABLE IF NOT EXISTS t_user_task_record (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    task_id BIGINT NOT NULL,
    task_date DATE NOT NULL,
    current_count INT DEFAULT 0,
    target_count INT DEFAULT 1,
    completed BOOLEAN DEFAULT FALSE,
    reward_claimed BOOLEAN DEFAULT FALSE,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_user_task_date (user_id, task_id, task_date)
);
