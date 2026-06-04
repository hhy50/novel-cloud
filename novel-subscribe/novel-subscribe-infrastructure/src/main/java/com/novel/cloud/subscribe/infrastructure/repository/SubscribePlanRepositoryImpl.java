package com.novel.cloud.subscribe.infrastructure.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.novel.cloud.subscribe.domain.entity.SubscribePlan;
import com.novel.cloud.subscribe.domain.repository.SubscribePlanRepository;
import com.novel.cloud.subscribe.infrastructure.dataobject.SubscribePlanDO;
import com.novel.cloud.subscribe.infrastructure.mapper.SubscribePlanMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SubscribePlanRepositoryImpl implements SubscribePlanRepository {

    private final SubscribePlanMapper subscribePlanMapper;

    @Override
    public List<SubscribePlan> listOnShelfPlans() {
        return subscribePlanMapper.selectList(
                        new LambdaQueryWrapper<SubscribePlanDO>()
                                .eq(SubscribePlanDO::getStatus, 1)
                                .orderByAsc(SubscribePlanDO::getSortOrder))
                .stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    @Override
    public SubscribePlan findById(Long id) {
        SubscribePlanDO planDO = subscribePlanMapper.selectById(id);
        return planDO != null ? toEntity(planDO) : null;
    }

    @Override
    public List<SubscribePlan> findByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return List.of();
        }
        return subscribePlanMapper.selectBatchIds(ids)
                .stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    private SubscribePlan toEntity(SubscribePlanDO planDO) {
        SubscribePlan entity = new SubscribePlan();
        BeanUtils.copyProperties(planDO, entity);
        return entity;
    }
}
