package com.novel.cloud.user.domain.service;

import com.novel.cloud.user.domain.entity.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 用户领域服务
 */
@Service
@RequiredArgsConstructor
public class UserDomainService {

    private static final String DEFAULT_AVATAR = "https://static.example.com/avatar/default.png";

    /**
     * 创建访客用户
     */
    public UserInfo createVisitorUser(Long id, String deviceId, String ip, String appVersion,
                                      String country, String language) {
        UserInfo user = new UserInfo();
        user.setId(id);
        user.setNickname("Visitor" + id);
        user.setAvatar(DEFAULT_AVATAR);
        user.setVipStatus(0);
        user.setDeviceId(deviceId);
        user.setIp(ip);
        user.setAppVersion(appVersion);
        user.setCountry(country);
        user.setLanguage(language);
        user.setLastLoginTime(LocalDateTime.now());
        return user;
    }

    /**
     * 更新登录信息
     */
    public void updateLoginInfo(UserInfo user, String ip, String appVersion) {
        user.setIp(ip);
        user.setAppVersion(appVersion);
        user.setLastLoginTime(LocalDateTime.now());
    }
}
