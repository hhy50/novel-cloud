package com.novel.cloud.user.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * Add user reward request DTO.
 */
@Data
public class UserRewardAddDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull(message = "userId cannot be null")
    private Long userId;

    private Integer coins;

    private Integer points;

    private String type;

    private String description;
}
