package nbc.newsfeed.domain.repository.newsfeed;

import nbc.newsfeed.domain.dto.newsfeeddto.NewsFeedResponseDto;
import nbc.newsfeed.domain.dto.newsfeeddto.NewsFeedSortType;

import java.util.List;

public interface NewsFeedRepositoryCustom {
    List<NewsFeedResponseDto> findFeedsWithSort(NewsFeedSortType sortType);
}