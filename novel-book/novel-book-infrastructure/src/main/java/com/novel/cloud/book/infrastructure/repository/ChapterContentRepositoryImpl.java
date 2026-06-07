package com.novel.cloud.book.infrastructure.repository;

import com.novel.cloud.book.domain.entity.ChapterContent;
import com.novel.cloud.book.domain.repository.ChapterContentRepository;
import com.novel.cloud.book.infrastructure.dataobject.ChapterContentDO;
import com.novel.cloud.book.infrastructure.mapper.ChapterContentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

/**
 * 章节正文仓储实现。
 * 路由细节封装在 {@link ChapterContentMapper}（SQL Provider 内拼接物理表名）。
 */
@Component
@RequiredArgsConstructor
public class ChapterContentRepositoryImpl implements ChapterContentRepository {

    private final ChapterContentMapper chapterContentMapper;

    @Override
    public ChapterContent findByBookIdAndChapterId(Long bookId, Long chapterId) {
        if (bookId == null || chapterId == null) {
            return null;
        }
        ChapterContentDO contentDO = chapterContentMapper.selectByBookIdAndChapterId(bookId, chapterId);
        if (contentDO == null) {
            return null;
        }
        ChapterContent content = new ChapterContent();
        BeanUtils.copyProperties(contentDO, content);
        return content;
    }
}
