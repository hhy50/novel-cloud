package com.novel.cloud.book.infrastructure.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.novel.cloud.book.infrastructure.dataobject.UserHistoryDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserHistoryMapper extends BaseMapper<UserHistoryDO> {
}
