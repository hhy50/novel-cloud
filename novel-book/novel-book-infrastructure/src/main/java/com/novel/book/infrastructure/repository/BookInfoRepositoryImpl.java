package com.novel.book.infrastructure.repository;

import com.novel.book.domain.entity.BookInfo;
import com.novel.book.domain.repository.BookInfoRepository;
import com.novel.book.infrastructure.dataobject.BookInfoDO;
import com.novel.book.infrastructure.mapper.BookInfoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

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
        BookInfo bookInfo = new BookInfo();
        BeanUtils.copyProperties(bookInfoDO, bookInfo);
        return bookInfo;
    }
}
