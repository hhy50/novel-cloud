package com.novel.user.domain.repository;

import com.novel.user.domain.entity.UserFeedback;

/**
 * User feedback repository interface
 */
public interface UserFeedbackRepository {

    void save(UserFeedback feedback);
}
