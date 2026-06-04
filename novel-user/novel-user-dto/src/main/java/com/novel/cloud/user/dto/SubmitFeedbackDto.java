package com.novel.cloud.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * Submit feedback request DTO
 */
@Data
public class SubmitFeedbackDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotBlank(message = "Category cannot be blank")
    private String category;

    @NotBlank(message = "Content cannot be blank")
    private String content;

    private String contact;
    private String images;
}
