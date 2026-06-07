package com.novel.cloud.book.infrastructure.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.novel.cloud.book.domain.entity.StoreCategoryStyle;
import com.novel.cloud.book.domain.repository.StoreCategoryStyleRepository;
import com.novel.cloud.book.infrastructure.dataobject.StoreCategoryStyleDO;
import com.novel.cloud.book.infrastructure.mapper.StoreCategoryStyleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class StoreCategoryStyleRepositoryImpl implements StoreCategoryStyleRepository {

    private final StoreCategoryStyleMapper storeCategoryStyleMapper;

    @Override
    public List<StoreCategoryStyle> findRootCategories(String appCode, String language) {
        List<StoreCategoryStyleDO> doList = storeCategoryStyleMapper.selectList(
                new LambdaQueryWrapper<StoreCategoryStyleDO>()
                        .eq(StoreCategoryStyleDO::getAppCode, appCode)
                        .eq(StoreCategoryStyleDO::getLanguage, language)
                        .isNull(StoreCategoryStyleDO::getPid)
                        .orderByAsc(StoreCategoryStyleDO::getId)
        );
        return doList.stream().map(this::toEntity).collect(Collectors.toList());
    }

    @Override
    public List<StoreCategoryStyle> findByPid(Long pid) {
        List<StoreCategoryStyleDO> doList = storeCategoryStyleMapper.selectList(
                new LambdaQueryWrapper<StoreCategoryStyleDO>()
                        .eq(StoreCategoryStyleDO::getPid, pid)
                        .orderByAsc(StoreCategoryStyleDO::getId)
        );
        return doList.stream().map(this::toEntity).collect(Collectors.toList());
    }

    private StoreCategoryStyle toEntity(StoreCategoryStyleDO d) {
        StoreCategoryStyle entity = new StoreCategoryStyle();
        BeanUtils.copyProperties(d, entity);
        return entity;
    }
}
