package com.novel.user.dto;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import lombok.Data;

/**
 * Daily tasks list response DTO
 */
@Data
public class DailyTasksVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private List<DailyTaskVo> tasks;
    private Integer completedCount;
    private Integer totalCount;
}
