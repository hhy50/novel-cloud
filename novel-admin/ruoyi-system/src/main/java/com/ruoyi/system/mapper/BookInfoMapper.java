package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.BookInfo;

/**
 * 小说信息Mapper接口
 *
 * @author ruoyi
 */
public interface BookInfoMapper
{
    /**
     * 查询小说信息
     *
     * @param id 小说信息主键
     * @return 小说信息
     */
    public BookInfo selectBookInfoById(Long id);

    /**
     * 查询小说信息列表
     *
     * @param bookInfo 小说信息
     * @return 小说信息集合
     */
    public List<BookInfo> selectBookInfoList(BookInfo bookInfo);

    /**
     * 新增小说信息
     *
     * @param bookInfo 小说信息
     * @return 结果
     */
    public int insertBookInfo(BookInfo bookInfo);

    /**
     * 修改小说信息
     *
     * @param bookInfo 小说信息
     * @return 结果
     */
    public int updateBookInfo(BookInfo bookInfo);

    /**
     * 删除小说信息
     *
     * @param id 小说信息主键
     * @return 结果
     */
    public int deleteBookInfoById(Long id);

    /**
     * 批量删除小说信息
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBookInfoByIds(Long[] ids);

    /**
     * 校验小说名称是否唯一
     *
     * @param name 小说名称
     * @return 结果
     */
    public BookInfo checkBookNameUnique(String name);
}
