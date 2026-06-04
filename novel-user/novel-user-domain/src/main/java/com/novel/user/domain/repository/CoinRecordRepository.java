package com.novel.user.domain.repository;

import com.novel.user.domain.entity.CoinRecord;
import java.util.List;

/**
 * Coin record repository interface
 */
public interface CoinRecordRepository {

    List<CoinRecord> findByUserId(Long userId, Integer page, Integer pageSize, String type);

    int countByUserId(Long userId, String type);

    void save(CoinRecord record);
}
