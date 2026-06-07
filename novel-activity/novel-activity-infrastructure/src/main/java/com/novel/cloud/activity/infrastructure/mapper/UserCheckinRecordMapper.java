package com.novel.cloud.activity.infrastructure.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.novel.cloud.activity.infrastructure.dataobject.UserCheckinRecordDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户签到记录Mapper
 */
@Mapper
public interface UserCheckinRecordMapper extends BaseMapper<UserCheckinRecordDO> {
}
