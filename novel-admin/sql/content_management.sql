-- ----------------------------
-- 小说分类表
-- ----------------------------
drop table if exists t_category;
create table t_category (
  id             bigint(20)      not null auto_increment    comment '分类ID',
  name           varchar(50)     default ''                 comment '分类名称',
  create_by      varchar(64)     default ''                 comment '创建者',
  create_time    datetime                                   comment '创建时间',
  update_by      varchar(64)     default ''                 comment '更新者',
  update_time    datetime                                   comment '更新时间',
  remark         varchar(500)    default ''                 comment '备注',
  primary key (id)
) engine=innodb auto_increment=1 comment = '小说分类表';

-- ----------------------------
-- 初始化-分类表数据
-- ----------------------------
insert into t_category values(1, '玄幻', 'admin', sysdate(), '', null, '玄幻类小说');
insert into t_category values(2, '仙侠', 'admin', sysdate(), '', null, '仙侠类小说');
insert into t_category values(3, '都市', 'admin', sysdate(), '', null, '都市类小说');
insert into t_category values(4, '历史', 'admin', sysdate(), '', null, '历史类小说');
insert into t_category values(5, '军事', 'admin', sysdate(), '', null, '军事类小说');
insert into t_category values(6, '游戏', 'admin', sysdate(), '', null, '游戏类小说');
insert into t_category values(7, '竞技', 'admin', sysdate(), '', null, '竞技类小说');
insert into t_category values(8, '科幻', 'admin', sysdate(), '', null, '科幻类小说');
insert into t_category values(9, '悬疑', 'admin', sysdate(), '', null, '悬疑类小说');
insert into t_category values(10, '其他', 'admin', sysdate(), '', null, '其他分类');

-- ----------------------------
-- 小说信息表
-- ----------------------------
drop table if exists t_book_info;
create table t_book_info (
  id               bigint(20)      not null auto_increment    comment '小说ID',
  language         varchar(20)     default 'zh-CN'            comment '语言',
  name             varchar(100)    not null                   comment '作品名称',
  author           varchar(50)     not null                   comment '作者',
  description      varchar(2000)   default ''                 comment '作品简介',
  total_price      int(11)         default 0                  comment '总定价（分）',
  words_price      int(11)         default 0                  comment '千字定价（分）',
  chapter_price    int(11)         default 0                  comment '章节定价（分）',
  free_chapters    int(11)         default 0                  comment '免费章节数',
  cover            varchar(500)    default ''                 comment '封面',
  status           int(1)          default 1                  comment '状态 1连载 2完结 3下架',
  online_status    int(1)          default 1                  comment '上架状态 1上架 2下架 3逻辑下架',
  is_baoyue        int(1)          default 0                  comment '是否会员作品 0非会员 1会员',
  is_hot           int(1)          default 0                  comment '是否热门 0否 1是',
  is_new           int(1)          default 0                  comment '是否新品 0否 1是',
  is_limited_free  int(1)          default 0                  comment '是否限时免费 0否 1是',
  is_greatest      int(1)          default 0                  comment '是否精选 0否 1是',
  is_god           int(1)          default 0                  comment '是否大神作品 0否 1是',
  total_words      int(11)         default 0                  comment '总字数',
  total_chapters   int(11)         default 0                  comment '章节数',
  total_views      int(11)         default 0                  comment '总浏览',
  total_favors     int(11)         default 0                  comment '总收藏数',
  score            int(3)          default 0                  comment '评分',
  last_chapter_id  int(11)         default null               comment '最新章节ID',
  last_chapter_time int(11)         default null               comment '最新章节时间 (Unix timestamp)',
  created_at       int(11)         default null               comment '创建时间 (Unix timestamp)',
  updated_at       int(11)         default null               comment '更新时间 (Unix timestamp)',
  deleted_at       int(11)         default null               comment '删除时间 (Unix timestamp)',
  create_by        varchar(64)     default ''                 comment '创建者',
  create_time      datetime                                   comment '创建时间',
  update_by        varchar(64)     default ''                 comment '更新者',
  update_time      datetime                                   comment '更新时间',
  remark           varchar(500)    default ''                 comment '备注',
  primary key (id)
) engine=innodb auto_increment=1 comment = '小说信息表';

-- ----------------------------
-- 小说分类关联表
-- ----------------------------
drop table if exists t_book_category;
create table t_book_category (
  id          bigint(20)      not null auto_increment    comment 'ID',
  book_id     bigint(20)      not null                   comment '小说ID',
  category_id bigint(20)      not null                   comment '分类ID',
  primary key (id),
  key idx_book_id (book_id),
  key idx_category_id (category_id)
) engine=innodb auto_increment=1 comment = '小说分类关联表';

-- ----------------------------
-- 字典类型：小说状态
-- ----------------------------
insert into sys_dict_type values(11, '小说状态', 'book_status', '0', 'admin', sysdate(), '', null, '小说状态列表');
insert into sys_dict_type values(12, '上架状态', 'book_online_status', '0', 'admin', sysdate(), '', null, '上架状态列表');

-- ----------------------------
-- 字典数据：小说状态
-- ----------------------------
insert into sys_dict_data values(100, 1, '连载',     '1', 'book_status',      '', 'primary', 'Y', '0', 'admin', sysdate(), '', null, '连载中');
insert into sys_dict_data values(101, 2, '完结',     '2', 'book_status',      '', 'success', 'Y', '0', 'admin', sysdate(), '', null, '已完结');
insert into sys_dict_data values(102, 3, '下架',     '3', 'book_status',      '', 'danger',  'N', '0', 'admin', sysdate(), '', null, '已下架');

-- ----------------------------
-- 字典数据：上架状态
-- ----------------------------
insert into sys_dict_data values(103, 1, '上架',     '1', 'book_online_status', '', 'primary', 'Y', '0', 'admin', sysdate(), '', null, '已上架');
insert into sys_dict_data values(104, 2, '下架',     '2', 'book_online_status', '', 'danger',  'N', '0', 'admin', sysdate(), '', null, '已下架');
insert into sys_dict_data values(105, 3, '逻辑下架', '3', 'book_online_status', '', 'warning', 'N', '0', 'admin', sysdate(), '', null, '逻辑下架');

-- ----------------------------
-- 内容管理菜单
-- ----------------------------
-- 一级菜单：内容管理
insert into sys_menu values(10000, '内容管理', '0', '4', '#', '', 'M', '0', '1', '', 'fa fa-book', 'admin', sysdate(), '', null, '内容管理目录');

-- 二级菜单：小说管理
insert into sys_menu values(10100, '小说管理', '10000', '1', '/content/book', '', 'C', '0', '1', 'content:book:view', 'fa fa-book', 'admin', sysdate(), '', null, '小说管理菜单');

-- 二级菜单：分类管理
insert into sys_menu values(10200, '分类管理', '10000', '2', '/content/category', '', 'C', '0', '1', 'content:category:view', 'fa fa-tags', 'admin', sysdate(), '', null, '分类管理菜单');

-- 小说管理按钮
insert into sys_menu values(10101, '小说查询', '10100', '1', '#', '', 'F', '0', '1', 'content:book:list', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values(10102, '小说新增', '10100', '2', '#', '', 'F', '0', '1', 'content:book:add', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values(10103, '小说修改', '10100', '3', '#', '', 'F', '0', '1', 'content:book:edit', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values(10104, '小说删除', '10100', '4', '#', '', 'F', '0', '1', 'content:book:remove', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values(10105, '小说导出', '10100', '5', '#', '', 'F', '0', '1', 'content:book:export', '#', 'admin', sysdate(), '', null, '');

-- 分类管理按钮
insert into sys_menu values(10201, '分类查询', '10200', '1', '#', '', 'F', '0', '1', 'content:category:list', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values(10202, '分类新增', '10200', '2', '#', '', 'F', '0', '1', 'content:category:add', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values(10203, '分类修改', '10200', '3', '#', '', 'F', '0', '1', 'content:category:edit', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values(10204, '分类删除', '10200', '4', '#', '', 'F', '0', '1', 'content:category:remove', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values(10205, '分类导出', '10200', '5', '#', '', 'F', '0', '1', 'content:category:export', '#', 'admin', sysdate(), '', null, '');

-- ----------------------------
-- 角色菜单关联
-- ----------------------------
-- 给超级管理员角色分配内容管理菜单
insert into sys_role_menu select '1', menu_id from sys_menu where menu_id >= 10000 and menu_id < 10300;

-- ----------------------------
-- 初始化一些示例小说数据
-- ----------------------------
insert into t_book_info (id, language, name, author, description, total_price, words_price, chapter_price, free_chapters, cover, status, online_status, is_baoyue, is_hot, is_new, is_limited_free, is_greatest, is_god, total_words, total_chapters, total_views, total_favors, score, created_at, updated_at, create_by, create_time, remark)
values(1, 'zh-CN', '斗破苍穹', '天蚕土豆', '三十年河东，三十年河西，莫欺少年穷！', 0, 10, 0, 50, '', 2, 1, 0, 1, 0, 0, 1, 1, 5300000, 1648, 99999999, 999999, 95, UNIX_TIMESTAMP(sysdate()), UNIX_TIMESTAMP(sysdate()), 'admin', sysdate(), '经典玄幻小说');

insert into t_book_info (id, language, name, author, description, total_price, words_price, chapter_price, free_chapters, cover, status, online_status, is_baoyue, is_hot, is_new, is_limited_free, is_greatest, is_god, total_words, total_chapters, total_views, total_favors, score, created_at, updated_at, create_by, create_time, remark)
values(2, 'zh-CN', '凡人修仙传', '忘语', '凡人流开山之作', 0, 12, 0, 100, '', 2, 1, 0, 1, 0, 0, 1, 1, 7700000, 2446, 99999999, 999999, 96, UNIX_TIMESTAMP(sysdate()), UNIX_TIMESTAMP(sysdate()), 'admin', sysdate(), '经典仙侠小说');

insert into t_book_info (id, language, name, author, description, total_price, words_price, chapter_price, free_chapters, cover, status, online_status, is_baoyue, is_hot, is_new, is_limited_free, is_greatest, is_god, total_words, total_chapters, total_views, total_favors, score, created_at, updated_at, create_by, create_time, remark)
values(3, 'zh-CN', '遮天', '辰东', '冰冷与黑暗并存的宇宙深处，九具庞大的龙尸拉着一口青铜古棺，极速飞掠...', 0, 15, 0, 30, '', 2, 1, 0, 1, 0, 0, 1, 1, 6350000, 1880, 99999999, 999999, 94, UNIX_TIMESTAMP(sysdate()), UNIX_TIMESTAMP(sysdate()), 'admin', sysdate(), '经典玄幻小说');

insert into t_book_info (id, language, name, author, description, total_price, words_price, chapter_price, free_chapters, cover, status, online_status, is_baoyue, is_hot, is_new, is_limited_free, is_greatest, is_god, total_words, total_chapters, total_views, total_favors, score, created_at, updated_at, create_by, create_time, remark)
values(4, 'zh-CN', '完美世界', '辰东', '一粒尘可填海，一根草斩尽日月星辰', 0, 12, 0, 20, '', 2, 1, 0, 1, 0, 0, 1, 1, 6150000, 2014, 99999999, 999999, 93, UNIX_TIMESTAMP(sysdate()), UNIX_TIMESTAMP(sysdate()), 'admin', sysdate(), '经典玄幻小说');

-- 分类关联
insert into t_book_category (book_id, category_id) values(1, 1);
insert into t_book_category (book_id, category_id) values(2, 2);
insert into t_book_category (book_id, category_id) values(3, 1);
insert into t_book_category (book_id, category_id) values(4, 1);
