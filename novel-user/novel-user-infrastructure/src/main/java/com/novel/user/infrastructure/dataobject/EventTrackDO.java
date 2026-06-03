package com.novel.user.infrastructure.dataobject;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import lombok.Data;

@Data
@TableName("t_event_track")
public class EventTrackDO {

    @TableId(type = IdType.INPUT)
    private Long id;

    private Long userId;

    private String event;

    private String eventData;

    private Long timestamp;

    private String deviceId;

    private String appVersion;

    private LocalDateTime createTime;
}
