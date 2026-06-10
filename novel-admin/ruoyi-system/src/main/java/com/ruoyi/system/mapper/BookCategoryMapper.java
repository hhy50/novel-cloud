package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.BookCategory;

/**
 * 小说分类Mapper接口
 *
 * @author ruoyi
 */
public interface BookCategoryMapper
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
     * 删除小说分类
     *
     * @param id 小说分类主键
     * @return 结果
     */
    public int deleteBookCategoryById(Long id);

    /**
     * 批量删除小说分类
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBookCategoryByIds(Long[] ids);

    /**
     * 校验分类名称是否唯一
     *
     * @param name 分类名称
     * @return 结果
     */
    public BookCategory checkCategoryNameUnique(String name);
}
