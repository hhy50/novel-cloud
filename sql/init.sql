CREATE DATABASE IF NOT EXISTS novel_user DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS novel_book DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE novel_user;

CREATE TABLE IF NOT EXISTS user_info (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    guest_id VARCHAR(64) NOT NULL,
    nickname VARCHAR(64) DEFAULT NULL,
    avatar VARCHAR(255) DEFAULT NULL,
    vip_status TINYINT DEFAULT 0,
    device_id VARCHAR(128) NOT NULL,
    device_name VARCHAR(128) DEFAULT NULL,
    os_type VARCHAR(32) DEFAULT NULL,
    app_version VARCHAR(32) DEFAULT NULL,
    region VARCHAR(64) DEFAULT NULL,
    ip VARCHAR(64) DEFAULT NULL,
    last_login_time DATETIME DEFAULT NULL,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uk_device_id (device_id),
    UNIQUE KEY uk_guest_id (guest_id)
);

INSERT INTO user_info (
    guest_id,
    nickname,
    avatar,
    vip_status,
    device_id,
    device_name,
    os_type,
    app_version,
    region,
    ip,
    last_login_time
)
VALUES (
    'guest_demo_001',
    '游客演示账号',
    'https://static.example.com/avatar/default.png',
    0,
    'demo-device-id',
    'Demo Device',
    'android',
    '1.0.0',
    'CN',
    '127.0.0.1',
    NOW()
);

USE novel_book;

CREATE TABLE IF NOT EXISTS book_info (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    book_name VARCHAR(128) NOT NULL,
    author_name VARCHAR(64) NOT NULL,
    category_name VARCHAR(64) DEFAULT NULL,
    cover_url VARCHAR(255) DEFAULT NULL,
    description TEXT,
    finished_status TINYINT DEFAULT 0,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

INSERT INTO book_info (book_name, author_name, category_name, cover_url, description, finished_status)
VALUES ('诡秘之主', '爱潜水的乌贼', '玄幻', 'https://static.example.com/book/cover/default.png', '示例书籍详情，后续可替换为数据库真实数据。', 1);
