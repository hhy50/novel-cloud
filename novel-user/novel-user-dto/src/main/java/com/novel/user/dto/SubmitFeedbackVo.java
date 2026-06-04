package com.novel.user.dto;

import java.io.Serial;
import java.io.Serializable;
import lombok.Data;

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
