package nbc.newsfeed.domain.repository.commentLike;

import nbc.newsfeed.domain.dto.newsfeedLike.LikeUserResponseDto;

import java.util.List;

public interface CommentLikeRepositoryCustom {
    List<LikeUserResponseDto> findLikeUsersByCommentId(Long newsId);
}
