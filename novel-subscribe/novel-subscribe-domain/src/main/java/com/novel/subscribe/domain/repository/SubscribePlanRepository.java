package com.novel.subscribe.domain.repository;

import com.novel.subscribe.domain.entity.SubscribePlan;

import java.util.List;

public interface SubscribePlanRepository {

    List<SubscribePlan> listOnShelfPlans();

    SubscribePlan findById(Long id);

    List<SubscribePlan> findByIds(List<Long> ids);
}
