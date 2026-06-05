package com.novel.cloud.user.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * Claim task reward request DTO
 */
@Data
public class ClaimTaskRewardDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull(message = "taskId cannot be null")
    private Long taskId;
}
