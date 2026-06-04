package com.novel.cloud.subscribe.domain.repository;

import com.novel.cloud.subscribe.domain.entity.SubscribePlan;

import java.util.List;

public interface SubscribePlanRepository {

    List<SubscribePlan> listOnShelfPlans();

    SubscribePlan findById(Long id);

    List<SubscribePlan> findByIds(List<Long> ids);
}
