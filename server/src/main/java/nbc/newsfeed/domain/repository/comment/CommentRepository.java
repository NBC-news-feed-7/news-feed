package nbc.newsfeed.domain.repository.comment;

import nbc.newsfeed.domain.entity.CommentEntity;
import nbc.newsfeed.domain.entity.NewsFeedEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
    Optional<List<CommentEntity>> findAllByNewsFeed(NewsFeedEntity newsFeed);
}
