package nbc.newsfeed.domain.repository.comment;

import nbc.newsfeed.domain.entity.CommentEntity;
import nbc.newsfeed.domain.entity.NewsFeedEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
    List<CommentEntity> findAllByNewsFeed(NewsFeedEntity newsFeed);
}
