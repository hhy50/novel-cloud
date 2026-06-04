-- ============================================================
-- Additional tables for missing features
-- ============================================================

-- ============================================================
-- novel_user database additions
-- ============================================================
USE `novel_user`;

-- User wallet table (coins and points)
CREATE TABLE IF NOT EXISTS `t_user_wallet` (
    `id`             BIGINT       NOT NULL AUTO_INCREMENT COMMENT 'Primary Key',
    `user_id`        BIGINT       NOT NULL                COMMENT 'User ID',
    `coins`          BIGINT       NOT NULL DEFAULT 0      COMMENT 'Coin balance',
    `points`         BIGINT       NOT NULL DEFAULT 0      COMMENT 'Points balance',
    `total_coins`    BIGINT       NOT NULL DEFAULT 0      COMMENT 'Total coins earned',
    `total_points`   BIGINT       NOT NULL DEFAULT 0      COMMENT 'Total points earned',
    `create_time`    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Create time',
    `update_time`    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Update time',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='User wallet table';

-- Coin transaction records
CREATE TABLE IF NOT EXISTS `t_coin_record` (
    `id`             BIGINT       NOT NULL AUTO_INCREMENT COMMENT 'Primary Key',
    `user_id`        BIGINT       NOT NULL                COMMENT 'User ID',
    `amount`         BIGINT       NOT NULL                COMMENT 'Amount (positive=income, negative=expense)',
    `balance`        BIGINT       NOT NULL                COMMENT 'Balance after transaction',
    `type`           VARCHAR(32)  NOT NULL                COMMENT 'Transaction type',
    `description`    VARCHAR(255) NOT NULL                COMMENT 'Description',
    `create_time`    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Create time',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Coin transaction records';

-- Daily check-in records
CREATE TABLE IF NOT EXISTS `t_daily_checkin` (
    `id`             BIGINT       NOT NULL AUTO_INCREMENT COMMENT 'Primary Key',
    `user_id`        BIGINT       NOT NULL                COMMENT 'User ID',
    `checkin_date`   DATE         NOT NULL                COMMENT 'Check-in date',
    `consecutive_days` INT        NOT NULL DEFAULT 1      COMMENT 'Consecutive check-in days',
    `reward_coins`   INT          NOT NULL                COMMENT 'Coins rewarded',
    `reward_points`  INT          NOT NULL                COMMENT 'Points rewarded',
    `create_time`    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Create time',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_date` (`user_id`, `checkin_date`),
    KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Daily check-in records';

-- Daily tasks
CREATE TABLE IF NOT EXISTS `t_daily_task` (
    `id`             BIGINT       NOT NULL AUTO_INCREMENT COMMENT 'Primary Key',
    `task_code`      VARCHAR(32)  NOT NULL                COMMENT 'Task code',
    `task_name`      VARCHAR(64)  NOT NULL                COMMENT 'Task name',
    `task_desc`      VARCHAR(255) NOT NULL                COMMENT 'Task description',
    `reward_coins`   INT          NOT NULL DEFAULT 0      COMMENT 'Coin reward',
    `reward_points`  INT          NOT NULL DEFAULT 0      COMMENT 'Points reward',
    `target_count`   INT          NOT NULL DEFAULT 1      COMMENT 'Target completion count',
    `sort_order`     INT          NOT NULL DEFAULT 0      COMMENT 'Sort order',
    `status`         TINYINT      NOT NULL DEFAULT 1      COMMENT 'Status: 0=disabled, 1=enabled',
    `create_time`    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Create time',
    `update_time`    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Update time',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_task_code` (`task_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Daily tasks';

-- User task completion records
CREATE TABLE IF NOT EXISTS `t_user_task_record` (
    `id`             BIGINT       NOT NULL AUTO_INCREMENT COMMENT 'Primary Key',
    `user_id`        BIGINT       NOT NULL                COMMENT 'User ID',
    `task_id`        BIGINT       NOT NULL                COMMENT 'Task ID',
    `task_date`      DATE         NOT NULL                COMMENT 'Task date',
    `current_count`  INT          NOT NULL DEFAULT 0      COMMENT 'Current completion count',
    `target_count`   INT          NOT NULL                COMMENT 'Target count',
    `completed`      TINYINT(1)   NOT NULL DEFAULT 0      COMMENT 'Completed flag',
    `reward_claimed` TINYINT(1)   NOT NULL DEFAULT 0      COMMENT 'Reward claimed flag',
    `create_time`    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Create time',
    `update_time`    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Update time',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_task_date` (`user_id`, `task_id`, `task_date`),
    KEY `idx_user_date` (`user_id`, `task_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='User task completion records';

-- User feedback
CREATE TABLE IF NOT EXISTS `t_user_feedback` (
    `id`             BIGINT       NOT NULL AUTO_INCREMENT COMMENT 'Primary Key',
    `user_id`        BIGINT       NOT NULL                COMMENT 'User ID',
    `category`       VARCHAR(32)  NOT NULL                COMMENT 'Feedback category',
    `content`        TEXT         NOT NULL                COMMENT 'Feedback content',
    `contact`        VARCHAR(128) DEFAULT NULL            COMMENT 'Contact info',
    `images`         TEXT         DEFAULT NULL            COMMENT 'Image URLs (JSON array)',
    `status`         TINYINT      NOT NULL DEFAULT 0      COMMENT 'Status: 0=pending, 1=processing, 2=resolved',
    `reply`          TEXT         DEFAULT NULL            COMMENT 'Admin reply',
    `create_time`    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Create time',
    `update_time`    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Update time',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='User feedback';

-- Initialize daily tasks
INSERT INTO `t_daily_task` (`task_code`, `task_name`, `task_desc`, `reward_coins`, `reward_points`, `target_count`, `sort_order`) VALUES
('DAILY_LOGIN', 'Daily Login', 'Login to the app', 10, 5, 1, 1),
('READ_MINUTES', 'Read for 30 Minutes', 'Read any book for 30 minutes', 20, 10, 1, 2),
('READ_CHAPTERS', 'Read 3 Chapters', 'Read any 3 chapters', 15, 8, 3, 3),
('ADD_BOOKSHELF', 'Add to Bookshelf', 'Add a book to your bookshelf', 5, 3, 1, 4),
('SHARE_BOOK', 'Share a Book', 'Share any book', 10, 5, 1, 5);


-- ============================================================
-- novel_book database additions
-- ============================================================
USE `novel_book`;

-- User bookshelf
CREATE TABLE IF NOT EXISTS `t_user_bookshelf` (
    `id`             BIGINT       NOT NULL AUTO_INCREMENT COMMENT 'Primary Key',
    `user_id`        BIGINT       NOT NULL                COMMENT 'User ID',
    `book_id`        BIGINT       NOT NULL                COMMENT 'Book ID',
    `last_chapter_id` BIGINT      DEFAULT NULL            COMMENT 'Last read chapter ID',
    `last_read_time` DATETIME     DEFAULT NULL            COMMENT 'Last read time',
    `create_time`    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Create time',
    `update_time`    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Update time',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_book` (`user_id`, `book_id`),
    KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='User bookshelf';

-- Reading history
CREATE TABLE IF NOT EXISTS `t_reading_history` (
    `id`             BIGINT       NOT NULL AUTO_INCREMENT COMMENT 'Primary Key',
    `user_id`        BIGINT       NOT NULL                COMMENT 'User ID',
    `book_id`        BIGINT       NOT NULL                COMMENT 'Book ID',
    `chapter_id`     BIGINT       NOT NULL                COMMENT 'Chapter ID',
    `progress`       INT          NOT NULL DEFAULT 0      COMMENT 'Reading progress (percentage)',
    `duration`       INT          NOT NULL DEFAULT 0      COMMENT 'Reading duration (seconds)',
    `create_time`    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Create time',
    `update_time`    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Update time',
    PRIMARY KEY (`id`),
    KEY `idx_user_book` (`user_id`, `book_id`),
    KEY `idx_user_time` (`user_id`, `create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Reading history';

-- Chapter purchase records
CREATE TABLE IF NOT EXISTS `t_chapter_purchase` (
    `id`             BIGINT       NOT NULL AUTO_INCREMENT COMMENT 'Primary Key',
    `user_id`        BIGINT       NOT NULL                COMMENT 'User ID',
    `book_id`        BIGINT       NOT NULL                COMMENT 'Book ID',
    `chapter_id`     BIGINT       NOT NULL                COMMENT 'Chapter ID',
    `cost_coins`     INT          NOT NULL                COMMENT 'Coins spent',
    `create_time`    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Create time',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_chapter` (`user_id`, `chapter_id`),
    KEY `idx_user_book` (`user_id`, `book_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Chapter purchase records';

-- Search history
CREATE TABLE IF NOT EXISTS `t_search_history` (
    `id`             BIGINT       NOT NULL AUTO_INCREMENT COMMENT 'Primary Key',
    `user_id`        BIGINT       NOT NULL                COMMENT 'User ID',
    `keyword`        VARCHAR(128) NOT NULL                COMMENT 'Search keyword',
    `search_count`   INT          NOT NULL DEFAULT 1      COMMENT 'Search count',
    `create_time`    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Create time',
    `update_time`    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Update time',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_keyword` (`user_id`, `keyword`),
    KEY `idx_user_time` (`user_id`, `update_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Search history';

-- Reading statistics
CREATE TABLE IF NOT EXISTS `t_reading_stats` (
    `id`             BIGINT       NOT NULL AUTO_INCREMENT COMMENT 'Primary Key',
    `user_id`        BIGINT       NOT NULL                COMMENT 'User ID',
    `stat_date`      DATE         NOT NULL                COMMENT 'Statistics date',
    `books_read`     INT          NOT NULL DEFAULT 0      COMMENT 'Number of books read',
    `chapters_read`  INT          NOT NULL DEFAULT 0      COMMENT 'Number of chapters read',
    `minutes_read`   INT          NOT NULL DEFAULT 0      COMMENT 'Minutes read',
    `create_time`    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Create time',
    `update_time`    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Update time',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_date` (`user_id`, `stat_date`),
    KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Reading statistics';
