CREATE DATABASE IF NOT EXISTS novel_user DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS novel_book DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE novel_user;

CREATE TABLE IF NOT EXISTS user_info (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(64) NOT NULL,
    password VARCHAR(128) NOT NULL,
    nickname VARCHAR(64) DEFAULT NULL,
    avatar VARCHAR(255) DEFAULT NULL,
    vip_status TINYINT DEFAULT 0,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

INSERT INTO user_info (username, password, nickname, avatar, vip_status)
VALUES ('demo', '123456', '演示用户', 'https://static.example.com/avatar/default.png', 1);

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
