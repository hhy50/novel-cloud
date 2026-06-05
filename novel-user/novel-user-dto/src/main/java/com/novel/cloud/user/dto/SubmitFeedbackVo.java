package com.novel.cloud.user.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * Submit feedback response DTO
 */
@Data
public class SubmitFeedbackVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Boolean success;
    private String message;
}
