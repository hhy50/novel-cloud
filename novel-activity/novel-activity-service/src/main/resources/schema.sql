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
