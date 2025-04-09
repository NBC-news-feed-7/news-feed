package nbc.newsfeed.domain.repository.newsfeedLike;

import nbc.newsfeed.domain.dto.newsfeed.NewsFeedPageResponseDto;
import nbc.newsfeed.domain.dto.newsfeedLike.LikeUserResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface NewsFeedLikeRepositoryCustom {
    List<LikeUserResponseDto> findLikeUsersByNewsId(Long newsId);
    Page<NewsFeedPageResponseDto> findLikedFeedsByUserId(Long userId, Pageable pageable);

}