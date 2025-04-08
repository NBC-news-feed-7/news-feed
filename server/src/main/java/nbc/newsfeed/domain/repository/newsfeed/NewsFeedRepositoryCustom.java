package nbc.newsfeed.domain.repository.newsfeed;

import nbc.newsfeed.domain.dto.newsfeeddto.NewsFeedResponseDto;
import nbc.newsfeed.domain.dto.newsfeeddto.NewsFeedSortType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NewsFeedRepositoryCustom {
    Page<NewsFeedResponseDto> findFeedsWithSort(NewsFeedSortType sortType, Pageable pageable);
}