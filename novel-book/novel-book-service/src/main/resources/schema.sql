-- ========================================
-- 书籍服务 Schema (H2 / MySQL 通用语法)
-- ========================================

-- 用户书架表
-- 唯一约束 (user_id, book_id)：一个用户对同一本书只保留一条书架记录，
-- 重复 add 时走 ON DUPLICATE / 业务层幂等逻辑刷新 last_chapter_id 与 last_read_time。
CREATE TABLE IF NOT EXISTS t_user_bookshelf (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    book_id BIGINT NOT NULL COMMENT '书籍ID',
    last_chapter_id BIGINT NULL COMMENT '最近阅读章节ID（可空，加入书架时未必读过）',
    last_read_time TIMESTAMP NULL COMMENT '最近一次阅读时间',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '加入书架时间',
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_user_book (user_id, book_id),
    KEY idx_user_update (user_id, update_time)
) COMMENT='用户书架表';

-- 阅读历史表
-- 每个 (user, book) 维护一条最新进度记录；切换章节/翻页时更新 chapter_id / progress / duration。
-- 唯一约束 (user_id, book_id) 保证一本书一条历史，列表按 update_time 倒序即"最近在读"。
CREATE TABLE IF NOT EXISTS t_reading_history (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    book_id BIGINT NOT NULL COMMENT '书籍ID',
    chapter_id BIGINT NOT NULL COMMENT '当前/最后阅读章节ID',
    progress INT DEFAULT 0 COMMENT '章节内阅读进度，0-100 百分比',
    duration INT DEFAULT 0 COMMENT '本次会话阅读时长（秒），由上报累加',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '首次阅读时间',
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '最近一次上报时间',
    UNIQUE KEY uk_user_book (user_id, book_id),
    KEY idx_user_update (user_id, update_time)
) COMMENT='阅读历史表';

-- 用户阅读流水表（每读一章插一行）
-- 与 t_reading_history 的区别：本表是流水（多行），t_reading_history 是最新进度（一书一行）。
-- 索引：
--   idx_user_book_time —— 支撑 "该用户该书最近一次读了哪一章"（startReading 的核心查询）
--   idx_user_time      —— 支撑 "我最近读过什么"（首页 / 阅读流水列表）
CREATE TABLE IF NOT EXISTS t_user_history (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    book_id BIGINT NOT NULL COMMENT '书籍ID',
    chapter_id BIGINT NOT NULL COMMENT '本次阅读的章节ID',
    progress INT DEFAULT 0 COMMENT '章节内进度 0-100；继续上次同一章时继承，否则归零',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '阅读发生时间',
    KEY idx_user_book_time (user_id, book_id, create_time),
    KEY idx_user_time (user_id, create_time)
) COMMENT='用户阅读流水表（每次读取章节追加一行）';


-- ========================================
-- 章节内容分表 n_chapter_content_0 .. n_chapter_content_9
-- 路由规则：book_id % 10 → 后缀，一本书的全部章节落在同一张物理表，
--          便于按书查询、批量导入和缓存预热；分表后单表数据量约为总量的 1/10。
-- 结构：与原 n_chapter_content 一致（id/book_id/wordscount/content + 时间戳）；
--      idx_book_id 用于"按书扫章节"，主键 id 用于按章节 id 精确点查。
-- delete_time 走逻辑删除（NULL 表示未删），代码 where 子句中固定带 IS NULL 过滤。
-- ========================================
CREATE TABLE IF NOT EXISTS n_chapter_content_0 (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '章节id',
    book_id BIGINT NOT NULL COMMENT '书籍id',
    wordscount INT NOT NULL COMMENT '字数',
    content TEXT NOT NULL COMMENT '内容',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    delete_time DATETIME DEFAULT NULL,
    PRIMARY KEY (id),
    KEY idx_book_id (book_id) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='章节内容分表-0';

CREATE TABLE IF NOT EXISTS n_chapter_content_1 (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '章节id',
    book_id BIGINT NOT NULL COMMENT '书籍id',
    wordscount INT NOT NULL COMMENT '字数',
    content TEXT NOT NULL COMMENT '内容',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    delete_time DATETIME DEFAULT NULL,
    PRIMARY KEY (id),
    KEY idx_book_id (book_id) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='章节内容分表-1';

CREATE TABLE IF NOT EXISTS n_chapter_content_2 (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '章节id',
    book_id BIGINT NOT NULL COMMENT '书籍id',
    wordscount INT NOT NULL COMMENT '字数',
    content TEXT NOT NULL COMMENT '内容',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    delete_time DATETIME DEFAULT NULL,
    PRIMARY KEY (id),
    KEY idx_book_id (book_id) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='章节内容分表-2';

CREATE TABLE IF NOT EXISTS n_chapter_content_3 (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '章节id',
    book_id BIGINT NOT NULL COMMENT '书籍id',
    wordscount INT NOT NULL COMMENT '字数',
    content TEXT NOT NULL COMMENT '内容',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    delete_time DATETIME DEFAULT NULL,
    PRIMARY KEY (id),
    KEY idx_book_id (book_id) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='章节内容分表-3';

CREATE TABLE IF NOT EXISTS n_chapter_content_4 (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '章节id',
    book_id BIGINT NOT NULL COMMENT '书籍id',
    wordscount INT NOT NULL COMMENT '字数',
    content TEXT NOT NULL COMMENT '内容',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    delete_time DATETIME DEFAULT NULL,
    PRIMARY KEY (id),
    KEY idx_book_id (book_id) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='章节内容分表-4';

CREATE TABLE IF NOT EXISTS n_chapter_content_5 (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '章节id',
    book_id BIGINT NOT NULL COMMENT '书籍id',
    wordscount INT NOT NULL COMMENT '字数',
    content TEXT NOT NULL COMMENT '内容',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    delete_time DATETIME DEFAULT NULL,
    PRIMARY KEY (id),
    KEY idx_book_id (book_id) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='章节内容分表-5';

CREATE TABLE IF NOT EXISTS n_chapter_content_6 (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '章节id',
    book_id BIGINT NOT NULL COMMENT '书籍id',
    wordscount INT NOT NULL COMMENT '字数',
    content TEXT NOT NULL COMMENT '内容',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    delete_time DATETIME DEFAULT NULL,
    PRIMARY KEY (id),
    KEY idx_book_id (book_id) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='章节内容分表-6';

CREATE TABLE IF NOT EXISTS n_chapter_content_7 (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '章节id',
    book_id BIGINT NOT NULL COMMENT '书籍id',
    wordscount INT NOT NULL COMMENT '字数',
    content TEXT NOT NULL COMMENT '内容',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    delete_time DATETIME DEFAULT NULL,
    PRIMARY KEY (id),
    KEY idx_book_id (book_id) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='章节内容分表-7';

CREATE TABLE IF NOT EXISTS n_chapter_content_8 (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '章节id',
    book_id BIGINT NOT NULL COMMENT '书籍id',
    wordscount INT NOT NULL COMMENT '字数',
    content TEXT NOT NULL COMMENT '内容',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    delete_time DATETIME DEFAULT NULL,
    PRIMARY KEY (id),
    KEY idx_book_id (book_id) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='章节内容分表-8';

CREATE TABLE IF NOT EXISTS n_chapter_content_9 (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '章节id',
    book_id BIGINT NOT NULL COMMENT '书籍id',
    wordscount INT NOT NULL COMMENT '字数',
    content TEXT NOT NULL COMMENT '内容',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    delete_time DATETIME DEFAULT NULL,
    PRIMARY KEY (id),
    KEY idx_book_id (book_id) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='章节内容分表-9';
