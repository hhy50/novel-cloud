package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.BookCategory;

/**
 * 小说分类Service接口
 *
 * @author ruoyi
 */
public interface IBookCategoryService
{
    /**
     * 查询小说分类
     *
     * @param id 小说分类主键
     * @return 小说分类
     */
    public BookCategory selectBookCategoryById(Long id);

    /**
     * 查询小说分类列表
     *
     * @param bookCategory 小说分类
     * @return 小说分类集合
     */
    public List<BookCategory> selectBookCategoryList(BookCategory bookCategory);

    /**
     * 新增小说分类
     *
     * @param bookCategory 小说分类
     * @return 结果
     */
    public int insertBookCategory(BookCategory bookCategory);

    /**
     * 修改小说分类
     *
     * @param bookCategory 小说分类
     * @return 结果
     */
    public int updateBookCategory(BookCategory bookCategory);

    /**
     * 批量删除小说分类
     *
     * @param ids 需要删除的小说分类主键集合
     * @return 结果
     */
    public int deleteBookCategoryByIds(Long[] ids);

    /**
     * 删除小说分类信息
     *
     * @param id 小说分类主键
     * @return 结果
     */
    public int deleteBookCategoryById(Long id);

    /**
     * 校验分类名称是否唯一
     *
     * @param bookCategory 分类信息
     * @return 结果
     */
    public boolean checkCategoryNameUnique(BookCategory bookCategory);
}
