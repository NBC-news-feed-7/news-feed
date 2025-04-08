package nbc.newsfeed.domain.repository.newsfeed;


import nbc.newsfeed.domain.dto.newsfeed.NewsFeedPageResponseDto;
import nbc.newsfeed.domain.dto.newsfeed.NewsFeedSortType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NewsFeedRepositoryCustom {
    Page<NewsFeedPageResponseDto> findFeedsWithSort(NewsFeedSortType sortType, Pageable pageable);
}