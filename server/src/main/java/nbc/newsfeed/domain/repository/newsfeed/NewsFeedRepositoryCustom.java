package nbc.newsfeed.domain.repository.newsfeed;


import nbc.newsfeed.domain.dto.newsfeed.NewsFeedPageResponseDto;
import nbc.newsfeed.domain.dto.newsfeed.NewsFeedSortType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface NewsFeedRepositoryCustom {
    Page<NewsFeedPageResponseDto> searchFeeds(String keyword, LocalDate startDate, LocalDate endDate, NewsFeedSortType sortType, Pageable pageable);

    Page<NewsFeedPageResponseDto> searchFriendFeeds(List<Long> friendIds, NewsFeedSortType sortType, Pageable pageable);
}