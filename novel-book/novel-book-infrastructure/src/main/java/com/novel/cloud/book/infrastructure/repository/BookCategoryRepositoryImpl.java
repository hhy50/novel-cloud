package com.novel.cloud.book.infrastructure.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.novel.cloud.book.domain.entity.BookCategory;
import com.novel.cloud.book.domain.entity.BookInfo;
import com.novel.cloud.book.domain.repository.BookCategoryRepository;
import com.novel.cloud.book.infrastructure.dataobject.BookCategoryDO;
import com.novel.cloud.book.infrastructure.dataobject.BookInfoDO;
import com.novel.cloud.book.infrastructure.mapper.BookCategoryMapper;
import com.novel.cloud.book.infrastructure.mapper.BookInfoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class BookCategoryRepositoryImpl implements BookCategoryRepository {

    private final BookCategoryMapper bookCategoryMapper;
    private final BookInfoMapper bookInfoMapper;

    @Override
    public List<BookCategory> findByCategoryId(Long categoryId) {
        List<BookCategoryDO> doList = bookCategoryMapper.selectList(
                new LambdaQueryWrapper<BookCategoryDO>()
                        .eq(BookCategoryDO::getCategoryId, categoryId)
        );
        return doList.stream().map(d -> {
            BookCategory entity = new BookCategory();
            BeanUtils.copyProperties(d, entity);
            return entity;
        }).collect(Collectors.toList());
    }

    @Override
    public List<BookInfo> findBooksByIds(List<Long> bookIds) {
        if (bookIds == null || bookIds.isEmpty()) {
            return Collections.emptyList();
        }
        List<BookInfoDO> doList = bookInfoMapper.selectBatchIds(bookIds);
        return doList.stream().map(d -> {
            BookInfo entity = new BookInfo();
            BeanUtils.copyProperties(d, entity);
            return entity;
        }).collect(Collectors.toList());
    }
}
