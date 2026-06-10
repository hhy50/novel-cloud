package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.mapper.BookInfoMapper;
import com.ruoyi.system.domain.BookInfo;
import com.ruoyi.system.service.IBookInfoService;

/**
 * 小说信息Service业务层处理
 *
 * @author ruoyi
 */
@Service
public class BookInfoServiceImpl implements IBookInfoService
{
    @Autowired
    private BookInfoMapper bookInfoMapper;

    /**
     * 查询小说信息
     *
     * @param id 小说信息主键
     * @return 小说信息
     */
    @Override
    public BookInfo selectBookInfoById(Long id)
    {
        return bookInfoMapper.selectBookInfoById(id);
    }

    /**
     * 查询小说信息列表
     *
     * @param bookInfo 小说信息
     * @return 小说信息
     */
    @Override
    public List<BookInfo> selectBookInfoList(BookInfo bookInfo)
    {
        return bookInfoMapper.selectBookInfoList(bookInfo);
    }

    /**
     * 新增小说信息
     *
     * @param bookInfo 小说信息
     * @return 结果
     */
    @Override
    public int insertBookInfo(BookInfo bookInfo)
    {
        if (bookInfo.getTotalWords() == null) {
            bookInfo.setTotalWords(0);
        }
        if (bookInfo.getTotalChapters() == null) {
            bookInfo.setTotalChapters(0);
        }
        if (bookInfo.getTotalViews() == null) {
            bookInfo.setTotalViews(0);
        }
        if (bookInfo.getTotalFavors() == null) {
            bookInfo.setTotalFavors(0);
        }
        if (bookInfo.getScore() == null) {
            bookInfo.setScore(0);
        }
        if (bookInfo.getIsBaoyue() == null) {
            bookInfo.setIsBaoyue(0);
        }
        if (bookInfo.getIsHot() == null) {
            bookInfo.setIsHot(0);
        }
        if (bookInfo.getIsNew() == null) {
            bookInfo.setIsNew(0);
        }
        if (bookInfo.getIsLimitedFree() == null) {
            bookInfo.setIsLimitedFree(0);
        }
        if (bookInfo.getIsGreatest() == null) {
            bookInfo.setIsGreatest(0);
        }
        if (bookInfo.getIsGod() == null) {
            bookInfo.setIsGod(0);
        }
        return bookInfoMapper.insertBookInfo(bookInfo);
    }

    /**
     * 修改小说信息
     *
     * @param bookInfo 小说信息
     * @return 结果
     */
    @Override
    public int updateBookInfo(BookInfo bookInfo)
    {
        return bookInfoMapper.updateBookInfo(bookInfo);
    }

    /**
     * 批量删除小说信息
     *
     * @param ids 需要删除的小说信息主键
     * @return 结果
     */
    @Override
    public int deleteBookInfoByIds(Long[] ids)
    {
        return bookInfoMapper.deleteBookInfoByIds(ids);
    }

    /**
     * 删除小说信息信息
     *
     * @param id 小说信息主键
     * @return 结果
     */
    @Override
    public int deleteBookInfoById(Long id)
    {
        return bookInfoMapper.deleteBookInfoById(id);
    }

    /**
     * 校验小说名称是否唯一
     *
     * @param bookInfo 小说信息
     * @return 结果
     */
    @Override
    public boolean checkBookNameUnique(BookInfo bookInfo)
    {
        Long id = StringUtils.isNull(bookInfo.getId()) ? -1L : bookInfo.getId();
        BookInfo info = bookInfoMapper.checkBookNameUnique(bookInfo.getName());
        if (StringUtils.isNotNull(info) && info.getId().longValue() != id.longValue())
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }
}
