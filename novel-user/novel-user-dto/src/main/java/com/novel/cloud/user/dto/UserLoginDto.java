package com.novel.cloud.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class UserLoginDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotBlank(message = "deviceId can not be blank")
    private String deviceId;

    private String deviceName;

    private String osType;

    private String appVersion;

    private String country;
    private String language;

    private String ip;
}
