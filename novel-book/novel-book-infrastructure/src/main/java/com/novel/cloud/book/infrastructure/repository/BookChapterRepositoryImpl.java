package com.novel.cloud.book.infrastructure.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.novel.cloud.book.domain.entity.BookChapter;
import com.novel.cloud.book.domain.repository.BookChapterRepository;
import com.novel.cloud.book.infrastructure.dataobject.BookChapterDO;
import com.novel.cloud.book.infrastructure.mapper.BookChapterMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 书籍章节仓储实现
 */
@Component
@RequiredArgsConstructor
public class BookChapterRepositoryImpl implements BookChapterRepository {

    private final BookChapterMapper bookChapterMapper;

    @Override
    public List<BookChapter> findByBookId(Long bookId, Integer limit) {
        LambdaQueryWrapper<BookChapterDO> wrapper = new LambdaQueryWrapper<BookChapterDO>()
                .eq(BookChapterDO::getBookId, bookId)
                .eq(BookChapterDO::getStatus, 1) // 只查询已发布的章节
                .orderByAsc(BookChapterDO::getNumber); // 按章节序号排序

        // 如果 limit 不是 -1，则添加限制
        if (limit != null && limit != -1) {
            wrapper.last("LIMIT " + limit);
        }

        List<BookChapterDO> chapterDOList = bookChapterMapper.selectList(wrapper);
        return chapterDOList.stream()
                .map(this::toDomainEntity)
                .collect(Collectors.toList());
    }

    @Override
    public BookChapter findById(Long chapterId) {
        BookChapterDO chapterDO = bookChapterMapper.selectById(chapterId);
        if (chapterDO == null) {
            return null;
        }
        return toDomainEntity(chapterDO);
    }

    @Override
    public List<BookChapter> findByNumberRange(Long bookId, Integer startNumber, Integer endNumber) {
        LambdaQueryWrapper<BookChapterDO> wrapper = new LambdaQueryWrapper<BookChapterDO>()
                .eq(BookChapterDO::getBookId, bookId)
                .eq(BookChapterDO::getStatus, 1) // 只查询已发布的章节
                .ge(BookChapterDO::getNumber, startNumber) // 大于等于起始序号
                .le(BookChapterDO::getNumber, endNumber) // 小于等于结束序号
                .orderByAsc(BookChapterDO::getNumber); // 按章节序号升序

        List<BookChapterDO> chapterDOList = bookChapterMapper.selectList(wrapper);
        return chapterDOList.stream()
                .map(this::toDomainEntity)
                .collect(Collectors.toList());
    }

    @Override
    public BookChapter findByBookIdAndChapterNo(Long bookId, Integer chapterNo) {
        LambdaQueryWrapper<BookChapterDO> wrapper = new LambdaQueryWrapper<BookChapterDO>()
                .eq(BookChapterDO::getBookId, bookId)
                .eq(BookChapterDO::getNumber, chapterNo) // 使用 number 字段
                .eq(BookChapterDO::getStatus, 1) // 只查询已发布的章节
                .eq(BookChapterDO::getDeleteTime, 0); // 未删除

        BookChapterDO chapterDO = bookChapterMapper.selectOne(wrapper);
        if (chapterDO == null) {
            return null;
        }
        return toDomainEntity(chapterDO);
    }

    @Override
    public BookChapter findMaxChapterByBookId(Long bookId) {
        LambdaQueryWrapper<BookChapterDO> wrapper = new LambdaQueryWrapper<BookChapterDO>()
                .eq(BookChapterDO::getBookId, bookId)
                .eq(BookChapterDO::getStatus, 1) // 只查询已发布的章节
                .orderByDesc(BookChapterDO::getNumber) // 按章节序号倒序
                .last("LIMIT 1"); // 只取第一条

        BookChapterDO chapterDO = bookChapterMapper.selectOne(wrapper);
        if (chapterDO == null) {
            return null;
        }
        return toDomainEntity(chapterDO);
    }

    private BookChapter toDomainEntity(BookChapterDO chapterDO) {
        BookChapter bookChapter = new BookChapter();
        BeanUtils.copyProperties(chapterDO, bookChapter);
        return bookChapter;
    }
}
