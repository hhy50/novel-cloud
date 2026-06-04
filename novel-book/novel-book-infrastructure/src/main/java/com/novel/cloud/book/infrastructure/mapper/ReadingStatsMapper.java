package com.novel.cloud.book.infrastructure.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.novel.cloud.book.infrastructure.dataobject.ReadingStatsDO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface ReadingStatsMapper extends BaseMapper<ReadingStatsDO> {

    @Select("SELECT user_id, " +
            "SUM(books_read) as books_read, " +
            "SUM(chapters_read) as chapters_read, " +
            "SUM(minutes_read) as minutes_read, " +
            "COUNT(DISTINCT stat_date) as days_active " +
            "FROM t_reading_stats " +
            "WHERE user_id = #{userId} " +
            "GROUP BY user_id")
    ReadingStatsDO selectSummaryByUserId(@Param("userId") Long userId);
}
