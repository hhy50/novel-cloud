package com.novel.cloud.book.domain.service;

import com.novel.cloud.book.domain.entity.BookInfo;
import com.novel.cloud.book.domain.entity.SearchHistory;
import com.novel.cloud.book.domain.repository.BookInfoRepository;
import com.novel.cloud.book.domain.repository.SearchHistoryRepository;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class SearchDomainService {

    private final SearchHistoryRepository searchHistoryRepository;
    private final BookInfoRepository bookInfoRepository;

    @Data
    @Builder
    public static class SearchResult {
        private List<BookInfo> books;
        private long total;
        private String keyword;
    }

    @Data
    @Builder
    public static class HotSearchItem {
        private int rank;
        private String keyword;
        private int searchCount;
    }

    @Data
    @Builder
    public static class HotSearchResult {
        private List<HotSearchItem> items;
    }

    @Data
    @Builder
    public static class RecommendCardItem {
        private BookInfo bookInfo;
    }

    @Data
    @Builder
    public static class RecommendCard {
        private String title;
        private String tagMode;
        private List<RecommendCardItem> items;
    }

    @Data
    @Builder
    public static class RecommendBooksResult {
        private List<RecommendCard> cards;
    }

    public SearchResult searchBooks(Long userId, String keyword, int page, int pageSize) {
        saveSearchHistory(userId, keyword);
        var pageResult = bookInfoRepository.searchByKeyword(keyword, page, pageSize);

        return SearchResult.builder()
                .books(pageResult.getRecords())
                .total(pageResult.getTotal())
                .keyword(keyword)
                .build();
    }

    public List<String> getSearchHistory(Long userId, int limit) {
        List<SearchHistory> historyList = searchHistoryRepository.findByUserId(userId, limit);
        return historyList.stream()
                .map(SearchHistory::getKeyword)
                .collect(Collectors.toList());
    }

    public void clearSearchHistory(Long userId) {
        searchHistoryRepository.deleteByUserId(userId);
    }

    public HotSearchResult getHotSearch(int limit) {
        List<SearchHistory> topHistory = searchHistoryRepository.findTopBySearchCount(limit);

        List<HotSearchItem> items = IntStream.range(0, topHistory.size())
                .mapToObj(i -> {
                    SearchHistory history = topHistory.get(i);
                    return HotSearchItem.builder()
                            .rank(i + 1)
                            .keyword(history.getKeyword())
                            .searchCount(history.getSearchCountOrDefault())
                            .build();
                })
                .collect(Collectors.toList());

        return HotSearchResult.builder()
                .items(items)
                .build();
    }

    public RecommendBooksResult getRecommendations(int limit) {
        List<BookInfo> recommendedBooks = bookInfoRepository.findRecommendations(limit);
        RecommendBooksResult result = RecommendBooksResult.builder()
                .cards(new java.util.ArrayList<>())
                .build();

        if (recommendedBooks.isEmpty()) {
            return result;
        }

        int mid = Math.min(recommendedBooks.size(), 6);
        List<BookInfo> topBooks = recommendedBooks.subList(0, mid);
        List<BookInfo> popularBooks = recommendedBooks.size() > mid
                ? recommendedBooks.subList(mid, Math.min(recommendedBooks.size(), mid + 6))
                : new java.util.ArrayList<>();

        RecommendCard topStoriesCard = RecommendCard.builder()
                .title("Top Stories")
                .tagMode("false")
                .items(topBooks.stream()
                        .map(book -> RecommendCardItem.builder().bookInfo(book).build())
                        .collect(Collectors.toList()))
                .build();

        RecommendCard popularSearchesCard = RecommendCard.builder()
                .title("Popular Searches")
                .tagMode("true")
                .items(popularBooks.stream()
                        .map(book -> RecommendCardItem.builder().bookInfo(book).build())
                        .collect(Collectors.toList()))
                .build();

        result.setCards(List.of(topStoriesCard, popularSearchesCard));
        return result;
    }

    private void saveSearchHistory(Long userId, String keyword) {
        SearchHistory existing = searchHistoryRepository.findByUserIdAndKeyword(userId, keyword);
        if (existing != null) {
            existing.incrementSearchCount();
            searchHistoryRepository.updateSearchCount(existing.getId(), existing.getSearchCountOrDefault());
        } else {
            SearchHistory history = new SearchHistory();
            history.setUserId(userId);
            history.setKeyword(keyword);
            history.setSearchCount(1);
            searchHistoryRepository.save(history);
        }
    }
}
