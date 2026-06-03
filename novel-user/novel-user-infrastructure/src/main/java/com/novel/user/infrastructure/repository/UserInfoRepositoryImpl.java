package com.novel.user.infrastructure.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.novel.user.domain.entity.UserInfo;
import com.novel.user.domain.repository.UserInfoRepository;
import com.novel.user.infrastructure.dataobject.UserInfoDO;
import com.novel.user.infrastructure.mapper.UserInfoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserInfoRepositoryImpl implements UserInfoRepository {

    private final UserInfoMapper userInfoMapper;

    @Override
    public UserInfo findByDeviceId(String deviceId) {
        UserInfoDO userInfoDO = userInfoMapper.selectOne(
                new LambdaQueryWrapper<UserInfoDO>()
                        .eq(UserInfoDO::getDeviceId, deviceId)
                        .last("limit 1")
        );
        if (userInfoDO == null) {
            return null;
        }
        UserInfo userInfo = new UserInfo();
        BeanUtils.copyProperties(userInfoDO, userInfo);
        return userInfo;
    }

    @Override
    public void save(UserInfo userInfo) {
        UserInfoDO userInfoDO = new UserInfoDO();
        BeanUtils.copyProperties(userInfo, userInfoDO);
        userInfoMapper.insert(userInfoDO);
    }

    @Override
    public void updateById(UserInfo userInfo) {
        UserInfoDO userInfoDO = new UserInfoDO();
        BeanUtils.copyProperties(userInfo, userInfoDO);
        userInfoMapper.updateById(userInfoDO);
    }
}
