package com.novel.user.domain.repository;

import com.novel.user.domain.entity.UserInfo;

/**
 * 用户信息仓储接口
 */
public interface UserInfoRepository {

    UserInfo findByDeviceId(String deviceId);

    void save(UserInfo userInfo);

    void updateById(UserInfo userInfo);
}
