package nbc.newsfeed.domain.repository.newsfeedLike;

import nbc.newsfeed.domain.dto.newsfeedLike.LikeUserResponseDto;

import java.util.List;

public interface NewsFeedLikeRepositoryCustom {
    List<LikeUserResponseDto> findLikeUsersByNewsId(Long newsId);
}