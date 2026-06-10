package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.mapper.BookCategoryMapper;
import com.ruoyi.system.domain.BookCategory;
import com.ruoyi.system.service.IBookCategoryService;

/**
 * 小说分类Service业务层处理
 *
 * @author ruoyi
 */
@Service
public class BookCategoryServiceImpl implements IBookCategoryService
{
    @Autowired
    private BookCategoryMapper bookCategoryMapper;

    /**
     * 查询小说分类
     *
     * @param id 小说分类主键
     * @return 小说分类
     */
    @Override
    public BookCategory selectBookCategoryById(Long id)
    {
        return bookCategoryMapper.selectBookCategoryById(id);
    }

    /**
     * 查询小说分类列表
     *
     * @param bookCategory 小说分类
     * @return 小说分类
     */
    @Override
    public List<BookCategory> selectBookCategoryList(BookCategory bookCategory)
    {
        return bookCategoryMapper.selectBookCategoryList(bookCategory);
    }

    /**
     * 新增小说分类
     *
     * @param bookCategory 小说分类
     * @return 结果
     */
    @Override
    public int insertBookCategory(BookCategory bookCategory)
    {
        return bookCategoryMapper.insertBookCategory(bookCategory);
    }

    /**
     * 修改小说分类
     *
     * @param bookCategory 小说分类
     * @return 结果
     */
    @Override
    public int updateBookCategory(BookCategory bookCategory)
    {
        return bookCategoryMapper.updateBookCategory(bookCategory);
    }

    /**
     * 批量删除小说分类
     *
     * @param ids 需要删除的小说分类主键
     * @return 结果
     */
    @Override
    public int deleteBookCategoryByIds(Long[] ids)
    {
        return bookCategoryMapper.deleteBookCategoryByIds(ids);
    }

    /**
     * 删除小说分类信息
     *
     * @param id 小说分类主键
     * @return 结果
     */
    @Override
    public int deleteBookCategoryById(Long id)
    {
        return bookCategoryMapper.deleteBookCategoryById(id);
    }

    /**
     * 校验分类名称是否唯一
     *
     * @param bookCategory 分类信息
     * @return 结果
     */
    @Override
    public boolean checkCategoryNameUnique(BookCategory bookCategory)
    {
        Long id = StringUtils.isNull(bookCategory.getId()) ? -1L : bookCategory.getId();
        BookCategory info = bookCategoryMapper.checkCategoryNameUnique(bookCategory.getName());
        if (StringUtils.isNotNull(info) && info.getId().longValue() != id.longValue())
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }
}
