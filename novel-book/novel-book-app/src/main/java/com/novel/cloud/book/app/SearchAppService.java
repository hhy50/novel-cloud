package com.novel.cloud.book.app;

import cn.dev33.satoken.stp.StpUtil;
import com.novel.cloud.book.domain.entity.BookInfo;
import com.novel.cloud.book.domain.entity.SearchHistory;
import com.novel.cloud.book.domain.repository.BookInfoRepository;
import com.novel.cloud.book.domain.repository.SearchHistoryRepository;
import com.novel.cloud.book.dto.*;
import com.novel.cloud.common.domain.PageResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Search application service
 */
@Service
@RequiredArgsConstructor
public class SearchAppService {

    private final SearchHistoryRepository searchHistoryRepository;
    private final BookInfoRepository bookInfoRepository;

    @Transactional
    public SearchBooksVo searchBooks(SearchBooksDto params) {
        Long userId = StpUtil.getLoginIdAsLong();
        // Save search history
        saveSearchHistory(userId, params.getKeyword());

        // Search books by keyword (single paginated query)
        PageResult<BookInfo> pageResult = bookInfoRepository.searchByKeyword(
                params.getKeyword(),
                params.getPage(),
                params.getPageSize()
        );

        List<SearchBookItemVo> itemVos = pageResult.getRecords().stream()
                .map(this::toSearchBookItemVo)
                .collect(Collectors.toList());

        SearchBooksVo result = new SearchBooksVo();
        result.setBooks(itemVos);
        result.setTotal((int) pageResult.getTotal());
        result.setKeyword(params.getKeyword());
        return result;
    }

    public SearchHistoryVo getSearchHistory() {
        Long userId = StpUtil.getLoginIdAsLong();
        List<SearchHistory> historyList = searchHistoryRepository.findByUserId(userId, 10);

        List<String> keywords = historyList.stream()
                .map(SearchHistory::getKeyword)
                .collect(Collectors.toList());

        SearchHistoryVo result = new SearchHistoryVo();
        result.setKeywords(keywords);
        return result;
    }

    @Transactional
    public Boolean clearSearchHistory() {
        Long userId = StpUtil.getLoginIdAsLong();
        searchHistoryRepository.deleteByUserId(userId);
        return true;
    }

    /**
     * Get hot search keywords ranked by search count
     */
    public HotSearchVo getHotSearch(Integer limit) {
        int effectiveLimit = limit != null ? limit : 10;
        List<SearchHistory> topHistory = searchHistoryRepository.findTopBySearchCount(effectiveLimit);

        List<HotSearchVo.HotSearchItem> items = IntStream.range(0, topHistory.size())
                .mapToObj(i -> {
                    SearchHistory history = topHistory.get(i);
                    HotSearchVo.HotSearchItem item = new HotSearchVo.HotSearchItem();
                    item.setRank(i + 1);
                    item.setKeyword(history.getKeyword());
                    item.setSearchCount(history.getSearchCount());
                    return item;
                })
                .collect(Collectors.toList());

        HotSearchVo result = new HotSearchVo();
        result.setItems(items);
        return result;
    }

    /**
     * Get recommended books for search page cards
     */
    public RecommendBooksVo getRecommendations() {
        List<BookInfo> recommendedBooks = bookInfoRepository.findRecommendations(20);

        RecommendBooksVo result = new RecommendBooksVo();

        if (recommendedBooks.isEmpty()) {
            result.setCards(new ArrayList<>());
            return result;
        }

        // Split into two cards: top stories and popular searches
        int mid = Math.min(recommendedBooks.size(), 6);
        List<BookInfo> topBooks = recommendedBooks.subList(0, mid);
        List<BookInfo> popularBooks = recommendedBooks.size() > mid
                ? recommendedBooks.subList(mid, Math.min(recommendedBooks.size(), mid + 6))
                : new ArrayList<>();

        RecommendBooksVo.RecommendCard topStoriesCard = new RecommendBooksVo.RecommendCard();
        topStoriesCard.setTitle("Top Stories");
        topStoriesCard.setTagMode("false");
        topStoriesCard.setItems(topBooks.stream()
                .map(this::toRecommendBookItem)
                .collect(Collectors.toList()));

        RecommendBooksVo.RecommendCard popularSearchesCard = new RecommendBooksVo.RecommendCard();
        popularSearchesCard.setTitle("Popular Searches");
        popularSearchesCard.setTagMode("true");
        popularSearchesCard.setItems(popularBooks.stream()
                .map(this::toRecommendBookItem)
                .collect(Collectors.toList()));

        result.setCards(List.of(topStoriesCard, popularSearchesCard));
        return result;
    }

    private void saveSearchHistory(Long userId, String keyword) {
        SearchHistory existing = searchHistoryRepository.findByUserIdAndKeyword(userId, keyword);
        if (existing != null) {
            // Update search count
            searchHistoryRepository.updateSearchCount(existing.getId(), existing.getSearchCount() + 1);
        } else {
            // Create new record
            SearchHistory history = new SearchHistory();
            history.setUserId(userId);
            history.setKeyword(keyword);
            history.setSearchCount(1);
            searchHistoryRepository.save(history);
        }
    }

    private SearchBookItemVo toSearchBookItemVo(BookInfo bookInfo) {
        SearchBookItemVo vo = new SearchBookItemVo();
        vo.setBookId(bookInfo.getId());
        vo.setBookName(bookInfo.getName());
        vo.setAuthorName(bookInfo.getAuthor());
        vo.setCategoryName(resolveCategoryName(bookInfo));
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

    private RecommendBooksVo.RecommendBookItem toRecommendBookItem(BookInfo bookInfo) {
        RecommendBooksVo.RecommendBookItem item = new RecommendBooksVo.RecommendBookItem();
        item.setBookId(bookInfo.getId());
        item.setTitle(bookInfo.getName());
        item.setSubtitle(bookInfo.getAuthor());
        item.setCoverUrl(bookInfo.getCover());
        return item;
    }

    private String resolveCategoryName(BookInfo bookInfo) {
        return "玄幻";
    }
}
