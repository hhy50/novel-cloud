package com.novel.user.dto;

import java.io.Serial;
import java.io.Serializable;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EventReportDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotBlank(message = "event can not be blank")
    private String event;

    /**
     * 字符串类型的 JSON 数据
     */
    private String eventData;

    @NotNull(message = "timestamp can not be null")
    private Long timestamp;

    private String deviceId;

    private String appVersion;
}
