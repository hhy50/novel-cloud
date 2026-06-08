package com.novel.cloud.book.app;

import cn.dev33.satoken.stp.StpUtil;
import com.novel.cloud.book.domain.entity.BookInfo;
import com.novel.cloud.book.domain.service.SearchDomainService;
import com.novel.cloud.book.dto.request.SearchBooksReq;
import com.novel.cloud.book.dto.response.HotSearchResp;
import com.novel.cloud.book.dto.response.RecommendBooksResp;
import com.novel.cloud.book.dto.response.SearchBooksResp;
import com.novel.cloud.book.dto.response.SearchHistoryResp;
import com.novel.cloud.book.dto.vo.HotSearchItemVo;
import com.novel.cloud.book.dto.vo.RecommendBookItemVo;
import com.novel.cloud.book.dto.vo.RecommendCardVo;
import com.novel.cloud.book.dto.vo.SearchBookItemVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchAppService {

    private final SearchDomainService searchDomainService;

    @Transactional
    public SearchBooksResp searchBooks(SearchBooksReq params) {
        Long userId = StpUtil.getLoginIdAsLong();
        SearchDomainService.SearchResult result = searchDomainService.searchBooks(
                userId, params.getKeyword(), params.getPage(), params.getPageSize());

        List<SearchBookItemVo> itemVos = result.getBooks().stream()
                .map(this::toSearchBookItemVo)
                .collect(Collectors.toList());

        SearchBooksResp vo = new SearchBooksResp();
        vo.setBooks(itemVos);
        vo.setTotal((int) result.getTotal());
        vo.setKeyword(result.getKeyword());
        return vo;
    }

    public SearchHistoryResp getSearchHistory() {
        Long userId = StpUtil.getLoginIdAsLong();
        List<String> keywords = searchDomainService.getSearchHistory(userId, 10);

        SearchHistoryResp result = new SearchHistoryResp();
        result.setKeywords(keywords);
        return result;
    }

    @Transactional
    public Boolean clearSearchHistory() {
        Long userId = StpUtil.getLoginIdAsLong();
        searchDomainService.clearSearchHistory(userId);
        return true;
    }

    public HotSearchResp getHotSearch(Integer limit) {
        SearchDomainService.HotSearchResult hotResult = searchDomainService.getHotSearch(
                limit != null ? limit : 10);

        List<HotSearchItemVo> items = hotResult.getItems().stream()
                .map(item -> {
                    HotSearchItemVo voItem = new HotSearchItemVo();
                    voItem.setRank(item.getRank());
                    voItem.setKeyword(item.getKeyword());
                    voItem.setSearchCount(item.getSearchCount());
                    return voItem;
                })
                .collect(Collectors.toList());

        HotSearchResp result = new HotSearchResp();
        result.setItems(items);
        return result;
    }

    public RecommendBooksResp getRecommendations() {
        SearchDomainService.RecommendBooksResult recResult = searchDomainService.getRecommendations(20);

        RecommendBooksResp result = new RecommendBooksResp();
        result.setCards(recResult.getCards().stream()
                .map(card -> {
                    RecommendCardVo voCard = new RecommendCardVo();
                    voCard.setTitle(card.getTitle());
                    voCard.setTagMode(card.getTagMode());
                    voCard.setItems(card.getItems().stream()
                            .map(item -> toRecommendBookItemVo(item.getBookInfo()))
                            .collect(Collectors.toList()));
                    return voCard;
                })
                .collect(Collectors.toList()));
        return result;
    }

    private SearchBookItemVo toSearchBookItemVo(BookInfo bookInfo) {
        SearchBookItemVo vo = new SearchBookItemVo();
        vo.setBookId(bookInfo.getId());
        vo.setBookName(bookInfo.getName());
        vo.setAuthorName(bookInfo.getAuthor());
        vo.setCategoryName(bookInfo.getName());
        vo.setCoverUrl(bookInfo.getCover());
        vo.setDescription(bookInfo.getDescription());
        vo.setStatus(bookInfo.getStatus());
        vo.setFinishedStatus(bookInfo.getStatus());
        vo.setTotalWords(bookInfo.getTotalWords());
        vo.setScore(bookInfo.getScore());
        vo.setIsHot(bookInfo.getIsHot());
        vo.setIsNew(bookInfo.getIsNew());
        vo.setIsBaoyue(bookInfo.getIsBaoyue());
        return vo;
    }

    private RecommendBookItemVo toRecommendBookItemVo(BookInfo bookInfo) {
        RecommendBookItemVo item = new RecommendBookItemVo();
        item.setBookId(bookInfo.getId());
        item.setTitle(bookInfo.getName());
        item.setSubtitle(bookInfo.getAuthor());
        item.setCoverUrl(bookInfo.getCover());
        return item;
    }
}
