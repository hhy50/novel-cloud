package com.novel.cloud.activity.infrastructure.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.novel.cloud.activity.infrastructure.dataobject.CheckinConfigDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 签到配置Mapper
 */
@Mapper
public interface CheckinConfigMapper extends BaseMapper<CheckinConfigDO> {
}
