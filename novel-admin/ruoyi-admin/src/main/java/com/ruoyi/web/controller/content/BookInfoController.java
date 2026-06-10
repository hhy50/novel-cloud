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
import com.ruoyi.system.domain.BookInfo;
import com.ruoyi.system.domain.BookCategory;
import com.ruoyi.system.service.IBookInfoService;
import com.ruoyi.system.service.IBookCategoryService;

/**
 * 小说管理Controller
 *
 * @author ruoyi
 */
@Controller
@RequestMapping("/content/book")
public class BookInfoController extends BaseController
{
    private String prefix = "content/book";

    @Autowired
    private IBookInfoService bookInfoService;

    @Autowired
    private IBookCategoryService bookCategoryService;

    @SaCheckPermission("content:book:view")
    @GetMapping()
    public String book()
    {
        return prefix + "/book";
    }

    /**
     * 查询小说管理列表
     */
    @SaCheckPermission("content:book:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(BookInfo bookInfo)
    {
        startPage();
        List<BookInfo> list = bookInfoService.selectBookInfoList(bookInfo);
        return getDataTable(list);
    }

    /**
     * 导出小说管理列表
     */
    @SaCheckPermission("content:book:export")
    @Log(title = "小说管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(BookInfo bookInfo)
    {
        List<BookInfo> list = bookInfoService.selectBookInfoList(bookInfo);
        ExcelUtil<BookInfo> util = new ExcelUtil<BookInfo>(BookInfo.class);
        return util.exportExcel(list, "小说数据");
    }

    /**
     * 新增小说
     */
    @SaCheckPermission("content:book:add")
    @GetMapping("/add")
    public String add(ModelMap mmap)
    {
        List<BookCategory> categories = bookCategoryService.selectBookCategoryList(new BookCategory());
        mmap.put("categories", categories);
        return prefix + "/add";
    }

    /**
     * 新增保存小说
     */
    @SaCheckPermission("content:book:add")
    @Log(title = "小说管理", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(@Validated BookInfo bookInfo)
    {
        if (!bookInfoService.checkBookNameUnique(bookInfo))
        {
            return error("新增小说'" + bookInfo.getName() + "'失败，小说名称已存在");
        }
        bookInfo.setCreateBy(getLoginName());
        return toAjax(bookInfoService.insertBookInfo(bookInfo));
    }

    /**
     * 修改小说
     */
    @SaCheckPermission("content:book:edit")
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        mmap.put("bookInfo", bookInfoService.selectBookInfoById(id));
        List<BookCategory> categories = bookCategoryService.selectBookCategoryList(new BookCategory());
        mmap.put("categories", categories);
        return prefix + "/edit";
    }

    /**
     * 修改保存小说
     */
    @SaCheckPermission("content:book:edit")
    @Log(title = "小说管理", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(@Validated BookInfo bookInfo)
    {
        if (!bookInfoService.checkBookNameUnique(bookInfo))
        {
            return error("修改小说'" + bookInfo.getName() + "'失败，小说名称已存在");
        }
        bookInfo.setUpdateBy(getLoginName());
        return toAjax(bookInfoService.updateBookInfo(bookInfo));
    }

    /**
     * 删除小说
     */
    @SaCheckPermission("content:book:remove")
    @Log(title = "小说管理", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(bookInfoService.deleteBookInfoByIds(Convert.toLongArray(ids)));
    }

    /**
     * 校验小说名称
     */
    @PostMapping("/checkBookNameUnique")
    @ResponseBody
    public boolean checkBookNameUnique(BookInfo bookInfo)
    {
        return bookInfoService.checkBookNameUnique(bookInfo);
    }
}
