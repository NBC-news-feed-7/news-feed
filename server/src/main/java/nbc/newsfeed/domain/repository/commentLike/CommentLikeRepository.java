package nbc.newsfeed.domain.repository.commentLike;

import nbc.newsfeed.domain.entity.CommentEntity;
import nbc.newsfeed.domain.entity.CommentLikeEntity;
import nbc.newsfeed.domain.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentLikeRepository extends JpaRepository<CommentLikeEntity, Long> {
    Optional<CommentLikeEntity> findByCommentAndUser(CommentEntity commentEntity, UserEntity user);

    int countByComment(Optional<CommentEntity> commentEntity);

}
