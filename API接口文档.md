# Novel-Cloud API 接口文档

## 文档说明

- **统一响应格式**：所有接口返回 `R<T>` 格式
- **认证方式**：Sa-Token JWT，Header 中携带 `Authorization: {token}`
- **时间格式**：`yyyy-MM-dd HH:mm:ss`
- **金额单位**：分（需转换为元展示）

### 统一响应格式

```json
{
  "code": 0,
  "message": "success",
  "data": {}
}
```

| 字段 | 类型 | 说明 |
|------|------|------|
| code | Integer | 状态码，0表示成功 |
| message | String | 提示信息 |
| data | Object | 业务数据 |

---

## 一、用户服务 (novel-user)

**服务端口**：8081  
**服务名称**：novel-user

### 1.1 用户登录

**接口地址**：`POST /api/user/login`

**接口说明**：游客登录接口，根据设备ID自动创建或登录用户

**请求参数**：

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| deviceId | String | 是 | 设备唯一标识 |
| deviceName | String | 否 | 设备名称 |
| osType | String | 否 | 操作系统类型 |
| appVersion | String | 否 | 应用版本号 |
| country | String | 否 | 国家/地区 |
| language | String | 否 | 语言 |
| ip | String | 否 | IP地址 |

**请求示例**：

```json
{
  "deviceId": "android-device-001",
  "deviceName": "Xiaomi 14",
  "osType": "android",
  "appVersion": "1.0.0",
  "country": "CN",
  "language": "zh-CN",
  "ip": "127.0.0.1"
}
```

**响应参数**：

| 字段 | 类型 | 说明 |
|------|------|------|
| userId | Long | 用户ID |
| guestId | String | 游客ID |
| nickname | String | 昵称 |
| avatar | String | 头像URL |
| vip | Boolean | 是否VIP |
| deviceId | String | 设备ID |
| region | String | 地区 |
| ip | String | IP地址 |
| token | String | 登录令牌 |

**响应示例**：

```json
{
  "code": 0,
  "message": "success",
  "data": {
    "userId": 1234567890,
    "guestId": "guest_1234567890",
    "nickname": "Visitor1234567890",
    "avatar": "https://static.example.com/avatar/default.png",
    "vip": false,
    "deviceId": "android-device-001",
    "region": "CN",
    "ip": "127.0.0.1",
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
  }
}
```

---

### 1.2 获取用户资料

**接口地址**：`GET /api/user/profile`

**接口说明**：获取当前登录用户的详细资料

**请求头**：
```
Authorization: {token}
```

**请求参数**：无

**响应参数**：

| 字段 | 类型 | 说明 |
|------|------|------|
| userId | Long | 用户ID |
| nickname | String | 昵称 |
| avatar | String | 头像URL |
| vip | Boolean | 是否VIP |
| vipExpireTime | String | VIP到期时间 |
| coins | Long | 金币余额 |

**响应示例**：

```json
{
  "code": 0,
  "message": "success",
  "data": {
    "userId": 1234567890,
    "nickname": "Visitor1234567890",
    "avatar": "https://static.example.com/avatar/default.png",
    "vip": true,
    "vipExpireTime": "2026-12-31 23:59:59",
    "coins": 10000
  }
}
```

---

### 1.3 获取钱包信息

**接口地址**：`GET /api/user/wallet`

**接口说明**：获取用户钱包信息（金币余额、VIP状态等）

**请求头**：
```
Authorization: {token}
```

**请求参数**：无

**响应参数**：

| 字段 | 类型 | 说明 |
|------|------|------|
| userId | Long | 用户ID |
| coins | Long | 金币余额 |
| vip | Boolean | 是否VIP |
| vipExpireTime | String | VIP到期时间 |

**响应示例**：

```json
{
  "code": 0,
  "message": "success",
  "data": {
    "userId": 1234567890,
    "coins": 10000,
    "vip": true,
    "vipExpireTime": "2026-12-31 23:59:59"
  }
}
```

---

### 1.4 获取金币记录

**接口地址**：`POST /api/user/coins/records`

**接口说明**：查询用户金币收支记录

**请求头**：
```
Authorization: {token}
```

**请求参数**：

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| pageNum | Integer | 否 | 页码，默认1 |
| pageSize | Integer | 否 | 每页条数，默认20 |
| type | Integer | 否 | 记录类型：1-收入 2-支出 |

**请求示例**：

```json
{
  "pageNum": 1,
  "pageSize": 20,
  "type": 1
}
```

**响应参数**：

| 字段 | 类型 | 说明 |
|------|------|------|
| total | Long | 总记录数 |
| records | Array | 记录列表 |
| records[].id | Long | 记录ID |
| records[].amount | Long | 金币数量 |
| records[].type | Integer | 类型：1-收入 2-支出 |
| records[].source | String | 来源说明 |
| records[].createTime | String | 创建时间 |

**响应示例**：

```json
{
  "code": 0,
  "message": "success",
  "data": {
    "total": 100,
    "records": [
      {
        "id": 1,
        "amount": 100,
        "type": 1,
        "source": "签到奖励",
        "createTime": "2026-06-05 10:00:00"
      }
    ]
  }
}
```

---

### 1.5 获取签到状态

**接口地址**：`GET /api/user/checkin/status`

**接口说明**：获取用户当前签到状态

**请求头**：
```
Authorization: {token}
```

**请求参数**：无

**响应参数**：

| 字段 | 类型 | 说明 |
|------|------|------|
| checkedToday | Boolean | 今日是否已签到 |
| continuousDays | Integer | 连续签到天数 |
| nextReward | Long | 明日签到奖励金币数 |

**响应示例**：

```json
{
  "code": 0,
  "message": "success",
  "data": {
    "checkedToday": false,
    "continuousDays": 5,
    "nextReward": 100
  }
}
```

---

### 1.6 执行签到

**接口地址**：`POST /api/user/checkin`

**接口说明**：用户每日签到，获取金币奖励

**请求头**：
```
Authorization: {token}
```

**请求参数**：无

**响应参数**：

| 字段 | 类型 | 说明 |
|------|------|------|
| reward | Long | 获得的金币数 |
| continuousDays | Integer | 连续签到天数 |
| totalCoins | Long | 当前金币余额 |

**响应示例**：

```json
{
  "code": 0,
  "message": "success",
  "data": {
    "reward": 100,
    "continuousDays": 6,
    "totalCoins": 10100
  }
}
```

---
### 1.7 获取每日任务列表

**接口地址**：`GET /api/user/tasks/daily`

**接口说明**：获取用户每日任务列表及完成状态

**请求头**：
```
Authorization: {token}
```

**请求参数**：无

**响应参数**：

| 字段 | 类型 | 说明 |
|------|------|------|
| tasks | Array | 任务列表 |
| tasks[].taskId | Long | 任务ID |
| tasks[].taskName | String | 任务名称 |
| tasks[].taskDesc | String | 任务描述 |
| tasks[].reward | Long | 奖励金币数 |
| tasks[].progress | Integer | 当前进度 |
| tasks[].target | Integer | 目标进度 |
| tasks[].completed | Boolean | 是否完成 |
| tasks[].claimed | Boolean | 是否已领取奖励 |

**响应示例**：

```json
{
  "code": 0,
  "message": "success",
  "data": {
    "tasks": [
      {
        "taskId": 1,
        "taskName": "阅读书籍",
        "taskDesc": "阅读任意书籍10分钟",
        "reward": 50,
        "progress": 5,
        "target": 10,
        "completed": false,
        "claimed": false
      }
    ]
  }
}
```

---

### 1.8 领取任务奖励

**接口地址**：`POST /api/user/tasks/claim`

**接口说明**：领取已完成任务的奖励

**请求头**：
```
Authorization: {token}
```

**请求参数**：

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| taskId | Long | 是 | 任务ID |

**请求示例**：

```json
{
  "taskId": 2
}
```

**响应参数**：

| 字段 | 类型 | 说明 |
|------|------|------|
| reward | Long | 获得的金币数 |
| totalCoins | Long | 当前金币余额 |

**响应示例**：

```json
{
  "code": 0,
  "message": "success",
  "data": {
    "reward": 100,
    "totalCoins": 10200
  }
}
```

---

### 1.9 提交用户反馈

**接口地址**：`POST /api/user/feedback`

**接口说明**：用户提交意见反馈

**请求头**：
```
Authorization: {token}
```

**请求参数**：

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| type | Integer | 是 | 反馈类型：1-功能建议 2-Bug反馈 3-其他 |
| content | String | 是 | 反馈内容 |
| contact | String | 否 | 联系方式 |

**请求示例**：

```json
{
  "type": 1,
  "content": "希望增加夜间模式",
  "contact": "user@example.com"
}
```

**响应参数**：

| 字段 | 类型 | 说明 |
|------|------|------|
| feedbackId | Long | 反馈记录ID |
| status | String | 提交状态 |

**响应示例**：

```json
{
  "code": 0,
  "message": "success",
  "data": {
    "feedbackId": 12345,
    "status": "已提交"
  }
}
```

---

### 1.10 上报用户事件

**接口地址**：`POST /api/user/event/report`

**接口说明**：上报用户行为事件（埋点）

**请求头**：
```
Authorization: {token}  （可选，未登录用户也可上报）
```

**请求参数**：

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| eventType | String | 是 | 事件类型 |
| eventData | Object | 否 | 事件数据（JSON） |
| deviceId | String | 是 | 设备ID |
| appVersion | String | 否 | 应用版本 |

**请求示例**：

```json
{
  "eventType": "page_view",
  "eventData": {
    "pageName": "bookDetail",
    "bookId": 123
  },
  "deviceId": "android-device-001",
  "appVersion": "1.0.0"
}
```

**响应参数**：无

**响应示例**：

```json
{
  "code": 0,
  "message": "success",
  "data": null
}
```

---

## 二、书籍服务 (novel-book)

**服务端口**：8082  
**服务名称**：novel-book

### 2.1 获取书籍详情

**接口地址**：`POST /api/book/detail`

**接口说明**：根据书籍ID获取书籍详细信息

**请求参数**：

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| bookId | Long | 是 | 书籍ID |

**请求示例**：

```json
{
  "bookId": 123
}
```

**响应参数**：

| 字段 | 类型 | 说明 |
|------|------|------|
| bookId | Long | 书籍ID |
| bookName | String | 书籍名称 |
| authorName | String | 作者名称 |
| categoryName | String | 分类名称 |
| coverUrl | String | 封面图片URL |
| description | String | 书籍简介 |
| finished | Boolean | 是否完结 |

**响应示例**：

```json
{
  "code": 0,
  "message": "success",
  "data": {
    "bookId": 123,
    "bookName": "斗破苍穹",
    "authorName": "天蚕土豆",
    "categoryName": "玄幻",
    "coverUrl": "https://static.example.com/cover/123.jpg",
    "description": "三十年河东，三十年河西，莫欺少年穷...",
    "finished": true
  }
}
```

---

### 2.2 获取章节列表

**接口地址**：`POST /api/book/chapters`

**接口说明**：获取书籍的章节列表

**请求参数**：

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| bookId | Long | 是 | 书籍ID |
| pageNum | Integer | 否 | 页码，默认1 |
| pageSize | Integer | 否 | 每页条数，默认100 |

**请求示例**：

```json
{
  "bookId": 123,
  "pageNum": 1,
  "pageSize": 100
}
```

**响应参数**：

| 字段 | 类型 | 说明 |
|------|------|------|
| total | Long | 总章节数 |
| chapters | Array | 章节列表 |
| chapters[].chapterId | Long | 章节ID |
| chapters[].chapterName | String | 章节名称 |
| chapters[].chapterOrder | Integer | 章节序号 |
| chapters[].isFree | Boolean | 是否免费 |
| chapters[].coinPrice | Long | 金币价格 |
| chapters[].purchased | Boolean | 是否已购买 |

**响应示例**：

```json
{
  "code": 0,
  "message": "success",
  "data": {
    "total": 1500,
    "chapters": [
      {
        "chapterId": 1001,
        "chapterName": "第一章 落魄少年",
        "chapterOrder": 1,
        "isFree": true,
        "coinPrice": 0,
        "purchased": true
      }
    ]
  }
}
```

---

### 2.3 获取章节内容

**接口地址**：`POST /api/book/chapter/content`

**接口说明**：获取章节正文内容

**请求头**：
```
Authorization: {token}
```

**请求参数**：

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| chapterId | Long | 是 | 章节ID |

**请求示例**：

```json
{
  "chapterId": 1001
}
```

**响应参数**：

| 字段 | 类型 | 说明 |
|------|------|------|
| chapterId | Long | 章节ID |
| chapterName | String | 章节名称 |
| content | String | 章节内容 |
| isFree | Boolean | 是否免费 |
| purchased | Boolean | 是否已购买 |

**响应示例**：

```json
{
  "code": 0,
  "message": "success",
  "data": {
    "chapterId": 1001,
    "chapterName": "第一章 落魄少年",
    "content": "这是一个属于斗气的世界...",
    "isFree": true,
    "purchased": true
  }
}
```

## 书籍服务补充接口

### 2.4 获取书城首页

**接口地址**：`POST /api/book/bookstore`

**接口说明**：获取书城首页推荐内容（分区展示）

**请求参数**：

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| userId | Long | 否 | 用户ID（用于个性化推荐） |

---

### 2.5 添加到书架

**接口地址**：`POST /api/book/bookshelf/add`

**请求头**：`Authorization: {token}`

**请求参数**：

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| bookId | Long | 是 | 书籍ID |

**请求示例**：

```json
{
  "bookId": 123
}
```

**响应示例**：

```json
{
  "code": 0,
  "message": "success",
  "data": true
}
```

---

### 2.6 从书架移除

**接口地址**：`POST /api/book/bookshelf/remove`

**请求头**：`Authorization: {token}`

**请求参数**：

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| bookId | Long | 是 | 书籍ID |

---

### 2.7 获取用户书架

**接口地址**：`GET /api/book/bookshelf`

**请求头**：`Authorization: {token}`

**响应参数**：

| 字段 | 类型 | 说明 |
|------|------|------|
| books | Array | 书架书籍列表 |
| books[].bookId | Long | 书籍ID |
| books[].bookName | String | 书籍名称 |
| books[].lastReadChapter | String | 最后阅读章节 |

---

### 2.8 搜索书籍

**接口地址**：`POST /api/book/search`

**请求参数**：

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| keyword | String | 是 | 搜索关键词 |
| pageNum | Integer | 否 | 页码，默认1 |
| pageSize | Integer | 否 | 每页条数，默认20 |

---

### 2.9 获取搜索历史

**接口地址**：`GET /api/book/search/history`

**请求头**：`Authorization: {token}`

---

### 2.10 清空搜索历史

**接口地址**：`POST /api/book/search/history/clear`

**请求头**：`Authorization: {token}`

---

### 2.11 获取热搜榜

**接口地址**：`GET /api/book/search/hot`

**请求参数**：

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| limit | Integer | 否 | 返回数量，默认10 |

---

### 2.12 获取推荐书籍

**接口地址**：`GET /api/book/recommendations`

**请求头**：`Authorization: {token}`

---

### 2.13 获取阅读历史

**接口地址**：`POST /api/book/reading/history`

**请求头**：`Authorization: {token}`

**请求参数**：

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| pageNum | Integer | 否 | 页码 |
| pageSize | Integer | 否 | 每页条数 |

---

### 2.14 记录阅读进度

**接口地址**：`POST /api/book/reading/record`

**请求头**：`Authorization: {token}`

**请求参数**：

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| bookId | Long | 是 | 书籍ID |
| chapterId | Long | 是 | 章节ID |
| readDuration | Integer | 否 | 阅读时长（秒） |

---

### 2.15 获取阅读统计

**接口地址**：`GET /api/book/reading/stats`

**请求头**：`Authorization: {token}`

**响应参数**：

| 字段 | 类型 | 说明 |
|------|------|------|
| totalReadDays | Integer | 累计阅读天数 |
| totalReadDuration | Long | 累计阅读时长（分钟） |
| totalBooksRead | Integer | 累计阅读书籍数 |

---

### 2.16 购买章节

**接口地址**：`POST /api/book/chapter/purchase`

**请求头**：`Authorization: {token}`

**请求参数**：

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| chapterId | Long | 是 | 章节ID |

**响应参数**：

| 字段 | 类型 | 说明 |
|------|------|------|
| success | Boolean | 是否成功 |
| coinCost | Long | 消耗金币数 |
| remainingCoins | Long | 剩余金币 |

---

## 三、订阅服务 (novel-subscribe)

**服务端口**：8083  
**服务名称**：novel-subscribe

### 3.1 获取订阅计划列表

**接口地址**：`GET /api/subscribe/plans`

**响应参数**：

| 字段 | 类型 | 说明 |
|------|------|------|
| plans | Array | 订阅计划列表 |
| plans[].id | Long | 计划ID |
| plans[].planName | String | 计划名称 |
| plans[].planCode | String | 计划编码 |
| plans[].durationDays | Integer | 订阅时长（天） |
| plans[].price | Long | 价格（分） |
| plans[].originalPrice | Long | 原价（分） |

**响应示例**：

```json
{
  "code": 0,
  "message": "success",
  "data": [
    {
      "id": 1,
      "planName": "月度会员",
      "planCode": "VIP_MONTH",
      "durationDays": 30,
      "price": 1500,
      "originalPrice": 1500
    }
  ]
}
```

---

### 3.2 创建订阅

**接口地址**：`POST /api/subscribe/create`

**请求头**：`Authorization: {token}`

**请求参数**：

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| userId | Long | 是 | 用户ID |
| planId | Long | 是 | 订阅计划ID |
| payChannel | String | 是 | 支付渠道：alipay/wechat |

**请求示例**：

```json
{
  "userId": 1234567890,
  "planId": 1,
  "payChannel": "alipay"
}
```

**响应参数**：

| 字段 | 类型 | 说明 |
|------|------|------|
| id | Long | 订阅记录ID |
| userId | Long | 用户ID |
| planName | String | 订阅计划名称 |
| planCode | String | 订阅计划编码 |
| durationDays | Integer | 订阅时长（天） |
| startTime | String | 生效时间 |
| endTime | String | 到期时间 |
| status | Integer | 订阅状态：0-待支付 1-生效中 2-已过期 3-已取消 |
| autoRenew | Boolean | 是否自动续订 |
| createTime | String | 创建时间 |

---

### 3.3 查询当前订阅

**接口地址**：`GET /api/subscribe/active`

**请求头**：`Authorization: {token}`

**请求参数**：

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| userId | Long | 是 | 用户ID |

**请求示例**：
```
GET /api/subscribe/active?userId=1234567890
```

**响应参数**：同创建订阅接口的响应参数

---

### 3.4 查询订阅历史

**接口地址**：`GET /api/subscribe/history`

**请求头**：`Authorization: {token}`

**请求参数**：

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| userId | Long | 是 | 用户ID |

**响应参数**：

| 字段 | 类型 | 说明 |
|------|------|------|
| records | Array | 订阅记录列表 |

---

## 四、支付服务 (novel-payment)

**服务端口**：8084  
**服务名称**：novel-payment

### 4.1 查询支付订单

**接口地址**：`GET /api/payment/query`

**请求头**：`Authorization: {token}`

**请求参数**：

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| orderNo | String | 是 | 订单号 |

**响应参数**：

| 字段 | 类型 | 说明 |
|------|------|------|
| id | Long | 支付订单ID |
| orderNo | String | 订单号 |
| userId | Long | 用户ID |
| amount | Long | 金额（分） |
| payChannel | String | 支付渠道 |
| payStatus | Integer | 支付状态：0-待支付 1-已支付 2-失败 3-已退款 |
| subscribeId | Long | 关联订阅ID |
| createTime | String | 创建时间 |

**响应示例**：

```json
{
  "code": 0,
  "message": "success",
  "data": {
    "id": 20001,
    "orderNo": "PAY20260605100000001",
    "userId": 1234567890,
    "amount": 1500,
    "payChannel": "alipay",
    "payStatus": 1,
    "subscribeId": 10001,
    "createTime": "2026-06-05 10:00:00"
  }
}
```

---

### 4.2 查询用户支付订单列表

**接口地址**：`GET /api/payment/list`

**请求头**：`Authorization: {token}`

**请求参数**：

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| userId | Long | 是 | 用户ID |

**响应参数**：返回支付订单数组

---

## 五、状态码说明

| 状态码 | 说明 |
|--------|------|
| 0 | 成功 |
| 400 | 请求参数错误 |
| 401 | 未登录或token失效 |
| 403 | 无权限访问 |
| 404 | 资源不存在 |
| 500 | 服务器内部错误 |
| 1001 | 金币余额不足 |
| 1002 | VIP已过期 |
| 1003 | 章节未购买 |
| 2001 | 订阅计划不存在 |
| 2002 | 订阅已存在 |
| 3001 | 支付订单不存在 |
| 3002 | 支付失败 |

---

**文档版本**：v1.0  
**更新日期**：2026-06-05  
**维护者**：Novel-Cloud 开发团队
