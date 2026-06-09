package com.ruoyi.web.controller.tool;

import cn.dev33.satoken.annotation.SaCheckPermission;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.ruoyi.common.core.controller.BaseController;

/**
 * build 表单构建
 * 
 * @author ruoyi
 */
@Controller
@RequestMapping("/tool/build")
public class BuildController extends BaseController
{
    private String prefix = "tool/build";

    @SaCheckPermission("tool:build:view")
    @GetMapping()
    public String build()
    {
        return prefix + "/build";
    }
}
