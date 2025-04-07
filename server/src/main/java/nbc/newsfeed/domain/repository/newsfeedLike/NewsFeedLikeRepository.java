package nbc.newsfeed.domain.repository.newsfeedLike;

import nbc.newsfeed.domain.entity.NewsFeedLikeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsFeedLikeRepository extends JpaRepository<NewsFeedLikeEntity, Long> {
}
