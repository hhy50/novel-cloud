package com.novel.cloud.book.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 阅读历史 - 领域实体（一书一条记录）
 */
@Data
public class ReadingHistory {

    private Long id;
    private Long userId;
    private Long bookId;
    private Long chapterId;
    private Integer progress;
    private Integer duration;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    /**
     * 更新阅读进度
     */
    public void updateProgress(Integer newProgress, Integer additionalDuration) {
        this.progress = newProgress;
        if (additionalDuration != null && additionalDuration > 0) {
            if (this.duration == null) {
                this.duration = additionalDuration;
            } else {
                this.duration = this.duration + additionalDuration;
            }
        }
        this.updateTime = LocalDateTime.now();
    }

    /**
     * 判断是否继续阅读同一章节
     */
    public boolean isContinuingReading(Long currentChapterId) {
        return currentChapterId != null && currentChapterId.equals(this.chapterId);
    }

    /**
     * 获取进度值
     */
    public Integer getProgressValue() {
        return progress != null ? progress : 0;
    }

    /**
     * 获取时长值（秒）
     */
    public Integer getDurationValue() {
        return duration != null ? duration : 0;
    }

    /**
     * 判断是否读完
     */
    public boolean isFinished() {
        return progress != null && progress >= 100;
    }

    /**
     * 判断是否有阅读记录
     */
    public boolean hasProgress() {
        return progress != null && progress > 0;
    }
}
