package com.novel.cloud.book.app;

import cn.dev33.satoken.stp.StpUtil;
import com.novel.cloud.book.domain.entity.BookChapter;
import com.novel.cloud.book.domain.repository.BookChapterRepository;
import com.novel.cloud.book.domain.service.ReadingDomainService;
import com.novel.cloud.book.dto.request.RecordReadingReq;
import com.novel.cloud.book.dto.request.StartReadingReq;
import com.novel.cloud.book.dto.response.StartReadingResp;
import com.novel.cloud.book.dto.vo.BookChapterVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReadingAppService {

    private final ReadingDomainService readingDomainService;
    private final BookChapterRepository bookChapterRepository;

    @Transactional
    public StartReadingResp startReading(StartReadingReq params) {
        Long userId = StpUtil.getLoginIdAsLong();
        Long chapterId = readingDomainService.startReading(userId, params.getBookId(), params.getChapterId());

        StartReadingResp resp = new StartReadingResp();
        resp.setBookId(params.getBookId());

        if (chapterId != null) {
            BookChapter chapter = bookChapterRepository.findById(chapterId);
            if (chapter != null) {
                BookChapterVo chapterVo = new BookChapterVo();
                chapterVo.setChapterId(chapter.getId());
                chapterVo.setChapterTitle(chapter.getTitle());
                chapterVo.setChapterNumber(chapter.getNumber());
                chapterVo.setWordsCount(chapter.getWordsCount());
                chapterVo.setUpdateTime(chapter.getUpdateTime());
                resp.setLastedReadChapter(chapterVo);
            }
        }
        return resp;
    }

    @Transactional
    public Boolean record(RecordReadingReq params) {
        Long userId = StpUtil.getLoginIdAsLong();
        readingDomainService.recordReadingProgress(
                userId, params.getBookId(), params.getChapterId());
        return true;
    }
}
