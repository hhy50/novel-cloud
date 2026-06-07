package com.novel.cloud.activity.infrastructure.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.novel.cloud.activity.infrastructure.dataobject.UserCheckinWeekStatusDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户周签到状态Mapper
 */
@Mapper
public interface UserCheckinWeekStatusMapper extends BaseMapper<UserCheckinWeekStatusDO> {
}
