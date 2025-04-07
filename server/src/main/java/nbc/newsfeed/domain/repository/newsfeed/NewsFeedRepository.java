package nbc.newsfeed.domain.repository.newsfeed;

import nbc.newsfeed.domain.entity.NewsFeedEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsFeedRepository extends JpaRepository<NewsFeedEntity, Long> {
}
