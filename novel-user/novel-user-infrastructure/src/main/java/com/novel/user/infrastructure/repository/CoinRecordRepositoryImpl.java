package com.novel.user.infrastructure.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.novel.user.domain.entity.CoinRecord;
import com.novel.user.domain.repository.CoinRecordRepository;
import com.novel.user.infrastructure.dataobject.CoinRecordDO;
import com.novel.user.infrastructure.mapper.CoinRecordMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CoinRecordRepositoryImpl implements CoinRecordRepository {

    private final CoinRecordMapper coinRecordMapper;

    @Override
    public List<CoinRecord> findByUserId(Long userId, Integer page, Integer pageSize, String type) {
        LambdaQueryWrapper<CoinRecordDO> wrapper = new LambdaQueryWrapper<CoinRecordDO>()
                .eq(CoinRecordDO::getUserId, userId);

        if (StringUtils.hasText(type)) {
            wrapper.eq(CoinRecordDO::getType, type);
        }

        Page<CoinRecordDO> pageResult = coinRecordMapper.selectPage(
                new Page<>(page, pageSize),
                wrapper.orderByDesc(CoinRecordDO::getCreateTime)
        );

        return pageResult.getRecords().stream()
                .map(recordDO -> {
                    CoinRecord record = new CoinRecord();
                    BeanUtils.copyProperties(recordDO, record);
                    return record;
                })
                .collect(Collectors.toList());
    }

    @Override
    public int countByUserId(Long userId, String type) {
        LambdaQueryWrapper<CoinRecordDO> wrapper = new LambdaQueryWrapper<CoinRecordDO>()
                .eq(CoinRecordDO::getUserId, userId);

        if (StringUtils.hasText(type)) {
            wrapper.eq(CoinRecordDO::getType, type);
        }

        return coinRecordMapper.selectCount(wrapper).intValue();
    }

    @Override
    public void save(CoinRecord record) {
        CoinRecordDO recordDO = new CoinRecordDO();
        BeanUtils.copyProperties(record, recordDO);
        coinRecordMapper.insert(recordDO);
    }
}
