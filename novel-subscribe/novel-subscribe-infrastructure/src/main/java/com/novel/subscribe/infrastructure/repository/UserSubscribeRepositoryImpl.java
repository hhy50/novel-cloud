package com.novel.subscribe.infrastructure.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.novel.subscribe.domain.entity.UserSubscribe;
import com.novel.subscribe.domain.repository.UserSubscribeRepository;
import com.novel.subscribe.infrastructure.dataobject.UserSubscribeDO;
import com.novel.subscribe.infrastructure.mapper.UserSubscribeMapper;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserSubscribeRepositoryImpl implements UserSubscribeRepository {

    private final UserSubscribeMapper userSubscribeMapper;

    @Override
    public UserSubscribe save(UserSubscribe userSubscribe) {
        UserSubscribeDO subDO = toDO(userSubscribe);
        userSubscribeMapper.insert(subDO);
        userSubscribe.setId(subDO.getId());
        return userSubscribe;
    }

    @Override
    public UserSubscribe update(UserSubscribe userSubscribe) {
        UserSubscribeDO subDO = toDO(userSubscribe);
        userSubscribeMapper.updateById(subDO);
        return userSubscribe;
    }

    @Override
    public UserSubscribe findById(Long id) {
        UserSubscribeDO subDO = userSubscribeMapper.selectById(id);
        return subDO != null ? toEntity(subDO) : null;
    }

    @Override
    public UserSubscribe findActiveByUserId(Long userId) {
        UserSubscribeDO subDO = userSubscribeMapper.selectOne(
                new LambdaQueryWrapper<UserSubscribeDO>()
                        .eq(UserSubscribeDO::getUserId, userId)
                        .eq(UserSubscribeDO::getStatus, UserSubscribe.STATUS_ACTIVE)
                        .orderByDesc(UserSubscribeDO::getEndTime)
                        .last("LIMIT 1"));
        return subDO != null ? toEntity(subDO) : null;
    }

    @Override
    public List<UserSubscribe> listByUserId(Long userId) {
        return userSubscribeMapper.selectList(
                        new LambdaQueryWrapper<UserSubscribeDO>()
                                .eq(UserSubscribeDO::getUserId, userId)
                                .orderByDesc(UserSubscribeDO::getCreateTime))
                .stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    private UserSubscribe toEntity(UserSubscribeDO subDO) {
        UserSubscribe entity = new UserSubscribe();
        BeanUtils.copyProperties(subDO, entity);
        return entity;
    }

    private UserSubscribeDO toDO(UserSubscribe entity) {
        UserSubscribeDO subDO = new UserSubscribeDO();
        BeanUtils.copyProperties(entity, subDO);
        return subDO;
    }
}
