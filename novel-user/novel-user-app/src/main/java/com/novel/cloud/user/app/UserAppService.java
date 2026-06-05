package com.novel.cloud.user.app;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.IdUtil;
import com.novel.cloud.user.domain.entity.UserInfo;
import com.novel.cloud.user.domain.repository.UserInfoRepository;
import com.novel.cloud.user.domain.service.UserDomainService;
import com.novel.cloud.user.dto.UserLoginDto;
import com.novel.cloud.user.dto.UserLoginVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * 用户应用服务
 */
@Service
@RequiredArgsConstructor
public class UserAppService {

    private final UserInfoRepository userInfoRepository;
    private final UserDomainService userDomainService;

    public Mono<UserLoginVo> login(UserLoginDto params) {
        return Mono.fromCallable(() -> doLogin(params));
    }

    private UserLoginVo doLogin(UserLoginDto params) {
        UserInfo userInfo = userInfoRepository.findByDeviceId(params.getDeviceId());
        if (userInfo == null) {
            // 新用户：创建访客
            Long userId = IdUtil.getSnowflakeNextId();
            userInfo = userDomainService.createVisitorUser(userId, params.getDeviceId(),
                    params.getIp(), params.getAppVersion(), params.getCountry(), params.getLanguage());
            StpUtil.login(userInfo.getId());
            userInfo.setToken(StpUtil.getTokenValueByLoginId(userInfo.getId()));
            userInfoRepository.save(userInfo);
        } else {
            // 老用户：更新登录信息
            userDomainService.updateLoginInfo(userInfo, params.getIp(), params.getAppVersion());
            userInfoRepository.updateById(userInfo);
        }

        UserLoginVo userLoginVo = new UserLoginVo()
                .setToken(userInfo.getToken())
                .setUserId(userInfo.getId())
                .setNickname(userInfo.getNickname());
        return userLoginVo;
    }
}
