package com.novel.user.dto;

import java.io.Serial;
import java.io.Serializable;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

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
