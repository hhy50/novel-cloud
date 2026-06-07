package com.novel.cloud.activity.infrastructure.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.novel.cloud.activity.domain.entity.CheckinConfig;
import com.novel.cloud.activity.domain.repository.CheckinConfigRepository;
import com.novel.cloud.activity.infrastructure.dataobject.CheckinConfigDO;
import com.novel.cloud.activity.infrastructure.mapper.CheckinConfigMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 签到配置仓储实现
 */
@Component
@RequiredArgsConstructor
public class CheckinConfigRepositoryImpl implements CheckinConfigRepository {

    private final CheckinConfigMapper checkinConfigMapper;

    @Override
    public CheckinConfig findActiveConfig() {
        LocalDateTime now = LocalDateTime.now();
        LambdaQueryWrapper<CheckinConfigDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CheckinConfigDO::getStatus, 1)
                .le(CheckinConfigDO::getStartTime, now)
                .and(w -> w.isNull(CheckinConfigDO::getEndTime).or().gt(CheckinConfigDO::getEndTime, now))
                .orderByDesc(CheckinConfigDO::getCreateTime)
                .last("limit 1");

        CheckinConfigDO configDO = checkinConfigMapper.selectOne(wrapper);
        return toEntity(configDO);
    }

    @Override
    public CheckinConfig findById(Long id) {
        CheckinConfigDO configDO = checkinConfigMapper.selectById(id);
        return toEntity(configDO);
    }

    @Override
    public void save(CheckinConfig config) {
        CheckinConfigDO configDO = toDO(config);
        checkinConfigMapper.insert(configDO);
        config.setId(configDO.getId());
    }

    @Override
    public void updateById(CheckinConfig config) {
        CheckinConfigDO configDO = toDO(config);
        checkinConfigMapper.updateById(configDO);
    }

    private CheckinConfig toEntity(CheckinConfigDO configDO) {
        if (configDO == null) {
            return null;
        }
        CheckinConfig config = new CheckinConfig();
        BeanUtils.copyProperties(configDO, config);
        return config;
    }

    private CheckinConfigDO toDO(CheckinConfig config) {
        if (config == null) {
            return null;
        }
        CheckinConfigDO configDO = new CheckinConfigDO();
        BeanUtils.copyProperties(config, configDO);
        return configDO;
    }
}
