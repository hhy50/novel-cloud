package com.ruoyi.web.controller.monitor;

import java.util.List;
import cn.dev33.satoken.annotation.SaMode;
import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.stp.StpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.enums.OnlineStatus;
import com.ruoyi.common.utils.SaTokenUtils;
import com.ruoyi.system.domain.SysUserOnline;
import com.ruoyi.system.service.ISysUserOnlineService;

/**
 * 在线用户监控
 * 
 * @author ruoyi
 */
@Controller
@RequestMapping("/monitor/online")
public class SysUserOnlineController extends BaseController
{
    private String prefix = "monitor/online";

    @Autowired
    private ISysUserOnlineService userOnlineService;

    @SaCheckPermission("monitor:online:view")
    @GetMapping()
    public String online()
    {
        return prefix + "/online";
    }

    @SaCheckPermission("monitor:online:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SysUserOnline userOnline)
    {
        startPage();
        List<SysUserOnline> list = userOnlineService.selectUserOnlineList(userOnline);
        return getDataTable(list);
    }

    @SaCheckPermission(value = { "monitor:online:batchForceLogout", "monitor:online:forceLogout" }, mode = SaMode.OR)
    @Log(title = "在线用户", businessType = BusinessType.FORCE)
    @PostMapping("/batchForceLogout")
    @ResponseBody
    public AjaxResult batchForceLogout(String ids)
    {
        for (String sessionId : Convert.toStrArray(ids))
        {
            SysUserOnline online = userOnlineService.selectOnlineById(sessionId);
            if (online == null)
            {
                return error("用户已下线");
            }
            // 获取当前登录的tokenValue
            String currentToken = StpUtil.getTokenValue();
            if (sessionId.equals(currentToken))
            {
                return error("当前登录用户无法强退");
            }
            // 使用Sa-Token强制下线
            try
            {
                StpUtil.logoutByTokenValue(sessionId);
            }
            catch (Exception e)
            {
                logger.error("强制下线失败: {}", e.getMessage());
            }
            
            // 更新在线状态
            online.setStatus(OnlineStatus.off_line);
            online.setSessionData(null);
            userOnlineService.saveOnline(online);
            userOnlineService.removeUserCache(online.getLoginName(), sessionId);
        }
        return success();
    }
}
