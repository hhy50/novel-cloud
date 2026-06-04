package com.novel.cloud.book.infrastructure.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.novel.cloud.book.domain.entity.SearchHistory;
import com.novel.cloud.book.domain.repository.SearchHistoryRepository;
import com.novel.cloud.book.infrastructure.dataobject.SearchHistoryDO;
import com.novel.cloud.book.infrastructure.mapper.SearchHistoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SearchHistoryRepositoryImpl implements SearchHistoryRepository {

    private final SearchHistoryMapper searchHistoryMapper;

    @Override
    public List<SearchHistory> findByUserId(Long userId, Integer limit) {
        List<SearchHistoryDO> historyDOList = searchHistoryMapper.selectList(
                new LambdaQueryWrapper<SearchHistoryDO>()
                        .eq(SearchHistoryDO::getUserId, userId)
                        .orderByDesc(SearchHistoryDO::getUpdateTime)
                        .last("limit " + limit)
        );

        return historyDOList.stream()
                .map(historyDO -> {
                    SearchHistory history = new SearchHistory();
                    BeanUtils.copyProperties(historyDO, history);
                    return history;
                })
                .collect(Collectors.toList());
    }

    @Override
    public SearchHistory findByUserIdAndKeyword(Long userId, String keyword) {
        SearchHistoryDO historyDO = searchHistoryMapper.selectOne(
                new LambdaQueryWrapper<SearchHistoryDO>()
                        .eq(SearchHistoryDO::getUserId, userId)
                        .eq(SearchHistoryDO::getKeyword, keyword)
                        .last("limit 1")
        );
        if (historyDO == null) {
            return null;
        }
        SearchHistory history = new SearchHistory();
        BeanUtils.copyProperties(historyDO, history);
        return history;
    }

    @Override
    public void save(SearchHistory history) {
        SearchHistoryDO historyDO = new SearchHistoryDO();
        BeanUtils.copyProperties(history, historyDO);
        searchHistoryMapper.insert(historyDO);
    }

    @Override
    public void updateSearchCount(Long id, Integer count) {
        SearchHistoryDO historyDO = new SearchHistoryDO();
        historyDO.setId(id);
        historyDO.setSearchCount(count);
        searchHistoryMapper.updateById(historyDO);
    }

    @Override
    public void deleteByUserId(Long userId) {
        searchHistoryMapper.delete(
                new LambdaQueryWrapper<SearchHistoryDO>()
                        .eq(SearchHistoryDO::getUserId, userId)
        );
    }

    @Override
    public List<SearchHistory> findTopBySearchCount(Integer limit) {
        List<SearchHistoryDO> historyDOList = searchHistoryMapper.selectList(
                new LambdaQueryWrapper<SearchHistoryDO>()
                        .orderByDesc(SearchHistoryDO::getSearchCount)
                        .last("limit " + limit)
        );

        return historyDOList.stream()
                .map(historyDO -> {
                    SearchHistory history = new SearchHistory();
                    BeanUtils.copyProperties(historyDO, history);
                    return history;
                })
                .collect(Collectors.toList());
    }
}
