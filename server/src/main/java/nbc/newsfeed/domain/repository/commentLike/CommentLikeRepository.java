package nbc.newsfeed.domain.repository.commentLike;

import nbc.newsfeed.common.error.CustomException;
import nbc.newsfeed.common.error.ErrorCode;
import nbc.newsfeed.domain.entity.CommentEntity;
import nbc.newsfeed.domain.entity.CommentLikeEntity;
import nbc.newsfeed.domain.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentLikeRepository extends JpaRepository<CommentLikeEntity, Long>, CommentLikeRepositoryCustom {
    Optional<CommentLikeEntity> findByCommentAndUser(CommentEntity commentEntity, UserEntity user);

    int countByComment(Optional<CommentEntity> commentEntity);

    boolean existsByCommentAndUser(CommentEntity commentEntity, UserEntity user);

    boolean existsByCommentIdAndUserId(Long commentId, Long userId);

    default void validateNotAlreadyLiked(CommentEntity commentEntity, UserEntity user) {
        if (existsByCommentAndUser(commentEntity, user)) {
            throw new CustomException(ErrorCode.ALREADY_LIKED);
        }
    }

    default CommentLikeEntity getByCommentAndUserOrThrow(CommentEntity commentEntity, UserEntity user) {
        return findByCommentAndUser(commentEntity, user)
                .orElseThrow(() -> new CustomException(ErrorCode.LIKE_NOT_FOUND));
    }

}
