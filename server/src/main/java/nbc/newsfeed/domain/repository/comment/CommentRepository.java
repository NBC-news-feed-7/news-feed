package nbc.newsfeed.domain.repository.comment;

import nbc.newsfeed.domain.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
}
