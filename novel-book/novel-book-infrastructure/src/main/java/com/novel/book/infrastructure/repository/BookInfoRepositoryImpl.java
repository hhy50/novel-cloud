package com.novel.book.infrastructure.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.novel.book.domain.entity.BookInfo;
import com.novel.book.domain.repository.BookInfoRepository;
import com.novel.book.infrastructure.dataobject.BookInfoDO;
import com.novel.book.infrastructure.mapper.BookInfoMapper;
import com.novel.common.core.domain.PageResult;
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
                .like(BookInfoDO::getBookName, likeKeyword)
                .or()
                .like(BookInfoDO::getAuthorName, likeKeyword)
                .or()
                .like(BookInfoDO::getDescription, likeKeyword)
                .orderByDesc(BookInfoDO::getUpdateTime);

        Page<BookInfoDO> pageResult = bookInfoMapper.selectPage(
                new Page<>(page, pageSize),
                wrapper
        );

        // IPage -> PageResult -> map 转换实体类型
        return PageResult.fromIPage(pageResult).map(this::toEntity);
    }

    @Override
    public List<BookInfo> findRecommendations(Integer limit) {
        LambdaQueryWrapper<BookInfoDO> wrapper = new LambdaQueryWrapper<BookInfoDO>()
                .orderByDesc(BookInfoDO::getUpdateTime)
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
