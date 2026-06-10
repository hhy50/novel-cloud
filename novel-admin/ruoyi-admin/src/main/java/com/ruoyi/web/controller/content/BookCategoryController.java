package com.ruoyi.web.controller.content;

import java.util.List;
import cn.dev33.satoken.annotation.SaCheckPermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.system.domain.BookCategory;
import com.ruoyi.system.service.IBookCategoryService;

/**
 * 小说分类Controller
 *
 * @author ruoyi
 */
@Controller
@RequestMapping("/content/category")
public class BookCategoryController extends BaseController
{
    private String prefix = "content/category";

    @Autowired
    private IBookCategoryService bookCategoryService;

    @SaCheckPermission("content:category:view")
    @GetMapping()
    public String category()
    {
        return prefix + "/category";
    }

    /**
     * 查询小说分类列表
     */
    @SaCheckPermission("content:category:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(BookCategory bookCategory)
    {
        startPage();
        List<BookCategory> list = bookCategoryService.selectBookCategoryList(bookCategory);
        return getDataTable(list);
    }

    /**
     * 导出小说分类列表
     */
    @SaCheckPermission("content:category:export")
    @Log(title = "小说分类", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(BookCategory bookCategory)
    {
        List<BookCategory> list = bookCategoryService.selectBookCategoryList(bookCategory);
        ExcelUtil<BookCategory> util = new ExcelUtil<BookCategory>(BookCategory.class);
        return util.exportExcel(list, "小说分类数据");
    }

    /**
     * 新增小说分类
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存小说分类
     */
    @SaCheckPermission("content:category:add")
    @Log(title = "小说分类", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(@Validated BookCategory bookCategory)
    {
        if (!bookCategoryService.checkCategoryNameUnique(bookCategory))
        {
            return error("新增分类'" + bookCategory.getName() + "'失败，分类名称已存在");
        }
        bookCategory.setCreateBy(getLoginName());
        return toAjax(bookCategoryService.insertBookCategory(bookCategory));
    }

    /**
     * 修改小说分类
     */
    @SaCheckPermission("content:category:edit")
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        mmap.put("bookCategory", bookCategoryService.selectBookCategoryById(id));
        return prefix + "/edit";
    }

    /**
     * 修改保存小说分类
     */
    @SaCheckPermission("content:category:edit")
    @Log(title = "小说分类", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(@Validated BookCategory bookCategory)
    {
        if (!bookCategoryService.checkCategoryNameUnique(bookCategory))
        {
            return error("修改分类'" + bookCategory.getName() + "'失败，分类名称已存在");
        }
        bookCategory.setUpdateBy(getLoginName());
        return toAjax(bookCategoryService.updateBookCategory(bookCategory));
    }

    /**
     * 删除小说分类
     */
    @SaCheckPermission("content:category:remove")
    @Log(title = "小说分类", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(bookCategoryService.deleteBookCategoryByIds(Convert.toLongArray(ids)));
    }

    /**
     * 校验分类名称
     */
    @PostMapping("/checkCategoryNameUnique")
    @ResponseBody
    public boolean checkCategoryNameUnique(BookCategory bookCategory)
    {
        return bookCategoryService.checkCategoryNameUnique(bookCategory);
    }
}
