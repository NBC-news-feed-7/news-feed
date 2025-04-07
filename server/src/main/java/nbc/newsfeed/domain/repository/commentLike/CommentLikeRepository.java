package nbc.newsfeed.domain.repository.commentLike;

import nbc.newsfeed.domain.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentLikeRepository extends JpaRepository<CommentEntity, Long> {
}
