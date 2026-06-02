package com.novel.user.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.novel.user.dto.UserLoginDto;
import com.novel.user.dto.UserLoginVo;
import com.novel.user.mapper.UserInfoMapper;
import com.novel.user.entity.UserInfoEntity;
import com.novel.user.service.UserService;

import java.time.LocalDateTime;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final String DEFAULT_AVATAR = "https://static.example.com/avatar/default.png";

    private final UserInfoMapper userInfoMapper;

    @Override
    public Mono<UserLoginVo> login(UserLoginDto params) {
        return Mono.fromCallable(() -> doLogin(params));
    }

    private UserLoginVo doLogin(UserLoginDto params) {
        UserInfoEntity userInfoEntity = userInfoMapper.selectOne(
                new LambdaQueryWrapper<UserInfoEntity>()
                        .eq(UserInfoEntity::getDeviceId, params.getDeviceId())
                        .last("limit 1")
        );
        if (userInfoEntity == null) {
            userInfoEntity = buildVisitUser(params);
            StpUtil.login(userInfoEntity.getId());
            userInfoEntity.setToken(StpUtil.getTokenValueByLoginId(userInfoEntity.getId()));
            userInfoMapper.insert(userInfoEntity);
        } else {
            userInfoEntity.setIp(params.getIp());
            userInfoEntity.setAppVersion(params.getAppVersion());
            userInfoEntity.setLastLoginTime(LocalDateTime.now());
            userInfoMapper.updateById(userInfoEntity);
        }

        UserLoginVo userLoginVo = new UserLoginVo()
                .setToken(userInfoEntity.getToken())
                .setUserId(userInfoEntity.getId())
                .setNickname(userInfoEntity.getNickname());
        return userLoginVo;
    }

    private UserInfoEntity buildVisitUser(UserLoginDto params) {
        UserInfoEntity userInfoEntity = new UserInfoEntity();

        userInfoEntity.setId(IdUtil.getSnowflakeNextId());
        userInfoEntity.setNickname("Visitor" + userInfoEntity.getId());
        userInfoEntity.setAvatar(DEFAULT_AVATAR);
        userInfoEntity.setVipStatus(0);
        userInfoEntity.setIp(params.getIp());
        userInfoEntity.setAppVersion(params.getAppVersion());
        userInfoEntity.setAppVersion(params.getAppVersion());
        userInfoEntity.setCountry(params.getCountry());
        userInfoEntity.setLanguage(params.getLanguage());
        userInfoEntity.setLastLoginTime(LocalDateTime.now());
        return userInfoEntity;
    }
}
