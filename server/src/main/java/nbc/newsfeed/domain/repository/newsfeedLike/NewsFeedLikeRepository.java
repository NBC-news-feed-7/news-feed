package nbc.newsfeed.domain.repository.newsfeedLike;

import nbc.newsfeed.domain.entity.NewsFeedEntity;
import nbc.newsfeed.domain.entity.NewsFeedLikeEntity;
import nbc.newsfeed.domain.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NewsFeedLikeRepository extends JpaRepository<NewsFeedLikeEntity, Long> {
    Optional<NewsFeedLikeEntity> findByNewsFeedAndUser(NewsFeedEntity newsFeed, UserEntity user);
    int countByNewsFeed(Optional<NewsFeedEntity> newsFeed);
}
