package com.novel.subscribe.domain.repository;

import com.novel.subscribe.domain.entity.UserSubscribe;

import java.util.List;

public interface UserSubscribeRepository {

    UserSubscribe save(UserSubscribe userSubscribe);

    UserSubscribe update(UserSubscribe userSubscribe);

    UserSubscribe findById(Long id);

    UserSubscribe findActiveByUserId(Long userId);

    List<UserSubscribe> listByUserId(Long userId);
}
