package com.novel.cloud.book.infrastructure.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.novel.cloud.book.domain.entity.BookInfo;
import com.novel.cloud.book.domain.repository.BookInfoRepository;
import com.novel.cloud.book.infrastructure.dataobject.BookInfoDO;
import com.novel.cloud.book.infrastructure.mapper.BookInfoMapper;
import com.novel.cloud.common.domain.PageResult;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class BookInfoRepositoryImpl implements BookInfoRepository {

    private final BookInfoMapper bookInfoMapper;

    @Override
    public BookInfo findById(Long id) {
        BookInfoDO bookInfoDO = bookInfoMapper.selectById(id);
        if (bookInfoDO == null) {
            return null;
        }
        return toEntity(bookInfoDO);
    }

    @Override
    public PageResult<BookInfo> searchByKeyword(String keyword, Integer page, Integer pageSize) {
        String likeKeyword = "%" + keyword + "%";
        LambdaQueryWrapper<BookInfoDO> wrapper = new LambdaQueryWrapper<BookInfoDO>()
                .like(BookInfoDO::getName, likeKeyword)
                .or()
                .like(BookInfoDO::getAuthor, likeKeyword)
                .or()
                .like(BookInfoDO::getDescription, likeKeyword)
                .orderByDesc(BookInfoDO::getUpdatedAt);

        Page<BookInfoDO> pageResult = bookInfoMapper.selectPage(
                new Page<>(page, pageSize),
                wrapper
        );

        return new PageResult<>(pageResult.getRecords(), pageResult.getTotal(), pageResult.getCurrent(), pageResult.getSize())
                .map(this::toEntity);
    }

    @Override
    public List<BookInfo> findRecommendations(Integer limit) {
        LambdaQueryWrapper<BookInfoDO> wrapper = new LambdaQueryWrapper<BookInfoDO>()
                .orderByDesc(BookInfoDO::getUpdatedAt)
                .last("limit " + limit);

        List<BookInfoDO> bookInfoDOList = bookInfoMapper.selectList(wrapper);
        return bookInfoDOList.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookInfo> findSimilarBooks(BookInfo currentBook, Integer limit) {
        LambdaQueryWrapper<BookInfoDO> wrapper = new LambdaQueryWrapper<BookInfoDO>()
                .ne(BookInfoDO::getId, currentBook.getId())
                .and(w -> w.isNull(BookInfoDO::getDeletedAt).or().eq(BookInfoDO::getDeletedAt, 0))
                .eq(BookInfoDO::getOnlineStatus, 1)
                .eq(currentBook.getLanguage() != null && !currentBook.getLanguage().isBlank(),
                        BookInfoDO::getLanguage, currentBook.getLanguage())
                .orderByDesc(BookInfoDO::getIsHot)
                .orderByDesc(BookInfoDO::getIsGreatest)
                .orderByDesc(BookInfoDO::getTotalViews)
                .orderByDesc(BookInfoDO::getUpdatedAt)
                .last("limit " + limit);

        List<BookInfoDO> bookInfoDOList = bookInfoMapper.selectList(wrapper);
        return bookInfoDOList.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    private BookInfo toEntity(BookInfoDO bookInfoDO) {
        BookInfo bookInfo = new BookInfo();
        BeanUtils.copyProperties(bookInfoDO, bookInfo);
        return bookInfo;
    }
}
