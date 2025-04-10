package nbc.newsfeed.domain.repository.comment;

import nbc.newsfeed.common.error.CustomException;
import nbc.newsfeed.common.error.ErrorCode;
import nbc.newsfeed.domain.entity.CommentEntity;
import nbc.newsfeed.domain.entity.NewsFeedEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
    Optional<List<CommentEntity>> findAllByNewsFeed(NewsFeedEntity newsFeed);

    default CommentEntity getByIdOrThrow(Long id) {
        return findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));
    }

    Optional<List<CommentEntity>> findAllByNewsFeedAndUseYn(NewsFeedEntity newsFeed, int i);


    Optional<CommentEntity> findByIdAndUseYn(Long parentCommentId, int i);
}
