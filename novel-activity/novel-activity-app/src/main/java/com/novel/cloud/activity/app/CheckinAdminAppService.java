package com.novel.cloud.activity.app;

import com.novel.cloud.activity.domain.entity.CheckinConfig;
import com.novel.cloud.activity.domain.repository.CheckinConfigRepository;
import com.novel.cloud.activity.dto.AdminUpdateCheckinConfigDto;
import com.novel.cloud.activity.dto.CheckinConfigVo;
import com.novel.cloud.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 签到配置管理（admin）应用服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CheckinAdminAppService {

    private final CheckinConfigRepository checkinConfigRepository;

    /**
     * 获取当前生效的签到配置
     */
    public CheckinConfigVo getActiveConfig() {
        CheckinConfig config = checkinConfigRepository.findActiveConfig();
        if (config == null) {
            throw new BusinessException(2003, "未找到生效的签到配置");
        }
        return toVo(config);
    }

    /**
     * 新建或更新签到配置
     * <p>dto.id == null → 新建；dto.id != null → 更新</p>
     */
    @Transactional(rollbackFor = Exception.class)
    public CheckinConfigVo saveConfig(AdminUpdateCheckinConfigDto dto) {
        CheckinConfig config = new CheckinConfig();
        BeanUtils.copyProperties(dto, config);

        if (dto.getId() == null) {
            checkinConfigRepository.save(config);
            log.info("Admin 新建签到配置: id={}, configCode={}", config.getId(), config.getConfigCode());
        } else {
            CheckinConfig existing = checkinConfigRepository.findById(dto.getId());
            if (existing == null) {
                throw new BusinessException(2004, "签到配置不存在: id=" + dto.getId());
            }
            checkinConfigRepository.updateById(config);
            log.info("Admin 更新签到配置: id={}, configCode={}", config.getId(), config.getConfigCode());
        }

        // 重新读取以拿到 createTime/updateTime
        CheckinConfig saved = checkinConfigRepository.findById(config.getId());
        return toVo(saved);
    }

    private CheckinConfigVo toVo(CheckinConfig config) {
        CheckinConfigVo vo = new CheckinConfigVo();
        BeanUtils.copyProperties(config, vo);
        return vo;
    }
}
