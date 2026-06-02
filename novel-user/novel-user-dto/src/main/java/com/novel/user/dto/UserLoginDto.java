package com.novel.user.dto;

import java.io.Serial;
import java.io.Serializable;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserLoginDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotBlank(message = "deviceId can not be blank")
    private String deviceId;

    private String deviceName;

    private String osType;

    private String appVersion;

    private String region;

    private String ip;
}
