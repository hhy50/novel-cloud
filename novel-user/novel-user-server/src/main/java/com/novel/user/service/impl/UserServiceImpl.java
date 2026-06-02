package com.novel.user.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.novel.user.dto.UserInfoVo;
import com.novel.user.dto.UserLoginDto;
import com.novel.user.dto.UserLoginVo;
import com.novel.user.mapper.UserInfoMapper;
import com.novel.user.model.entity.UserInfoEntity;
import com.novel.user.service.UserService;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final String DEFAULT_AVATAR = "https://static.example.com/avatar/default.png";

    private final UserInfoMapper userInfoMapper;

    @Override
    public Mono<UserLoginVo> login(UserLoginDto params) {
        return Mono.fromCallable(() -> doLogin(params))
                .subscribeOn(Schedulers.boundedElastic());
    }

    private UserLoginVo doLogin(UserLoginDto params) {
        UserInfoEntity userInfoEntity = userInfoMapper.selectOne(
                new LambdaQueryWrapper<UserInfoEntity>()
                        .eq(UserInfoEntity::getDeviceId, params.getDeviceId())
                        .last("limit 1")
        );

        if (userInfoEntity == null) {
            userInfoEntity = buildGuestUser(params);
            userInfoMapper.insert(userInfoEntity);
        } else {
            fillLoginContext(userInfoEntity, params);
            userInfoEntity.setLastLoginTime(LocalDateTime.now());
            userInfoMapper.updateById(userInfoEntity);
        }

        StpUtil.login(userInfoEntity.getId());

        UserInfoVo userInfoVo = new UserInfoVo();
        userInfoVo.setUserId(userInfoEntity.getId());
        userInfoVo.setGuestId(userInfoEntity.getGuestId());
        userInfoVo.setNickname(userInfoEntity.getNickname());
        userInfoVo.setAvatar(userInfoEntity.getAvatar());
        userInfoVo.setVip(userInfoEntity.getVipStatus() != null && userInfoEntity.getVipStatus() == 1);
        userInfoVo.setDeviceId(userInfoEntity.getDeviceId());
        userInfoVo.setRegion(userInfoEntity.getRegion());
        userInfoVo.setIp(userInfoEntity.getIp());

        UserLoginVo userLoginVo = new UserLoginVo();
        userLoginVo.setToken(StpUtil.getTokenValue());
        userLoginVo.setTokenName(StpUtil.getTokenName());
        userLoginVo.setExpireSeconds(StpUtil.getTokenTimeout());
        userLoginVo.setUserInfo(userInfoVo);
        return userLoginVo;
    }

    private UserInfoEntity buildGuestUser(UserLoginDto params) {
        UserInfoEntity userInfoEntity = new UserInfoEntity();
        userInfoEntity.setGuestId(generateGuestId());
        userInfoEntity.setNickname(generateNickname());
        userInfoEntity.setAvatar(DEFAULT_AVATAR);
        userInfoEntity.setVipStatus(0);
        fillLoginContext(userInfoEntity, params);
        userInfoEntity.setLastLoginTime(LocalDateTime.now());
        return userInfoEntity;
    }

    private void fillLoginContext(UserInfoEntity userInfoEntity, UserLoginDto params) {
        userInfoEntity.setDeviceId(params.getDeviceId());
        userInfoEntity.setDeviceName(params.getDeviceName());
        userInfoEntity.setOsType(params.getOsType());
        userInfoEntity.setAppVersion(params.getAppVersion());
        userInfoEntity.setRegion(params.getRegion());
        userInfoEntity.setIp(params.getIp());
    }

    private String generateGuestId() {
        return "guest_" + UUID.randomUUID().toString().replace("-", "");
    }

    private String generateNickname() {
        return "游客" + System.currentTimeMillis();
    }
}
