package nbc.newsfeed.domain.repository.newsfeedLike;

import nbc.newsfeed.common.error.CustomException;
import nbc.newsfeed.common.error.ErrorCode;
import nbc.newsfeed.domain.entity.NewsFeedEntity;
import nbc.newsfeed.domain.entity.NewsFeedLikeEntity;
import nbc.newsfeed.domain.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NewsFeedLikeRepository extends JpaRepository<NewsFeedLikeEntity, Long>, NewsFeedLikeRepositoryCustom {
    Optional<NewsFeedLikeEntity> findByNewsFeedAndUser(NewsFeedEntity newsFeed, UserEntity user);

    int countByNewsFeed(Optional<NewsFeedEntity> newsFeed);

    boolean existsByNewsFeedAndUser(NewsFeedEntity newsFeed, UserEntity user);

    default void validateNotAlreadyLiked(NewsFeedEntity newsFeed, UserEntity user) {
        if (existsByNewsFeedAndUser(newsFeed, user)) {
            throw new CustomException(ErrorCode.ALREADY_LIKED);
        }
    }

    default NewsFeedLikeEntity getByNewsFeedAndUserOrThrow(NewsFeedEntity newsFeed, UserEntity user) {
        return findByNewsFeedAndUser(newsFeed, user)
                .orElseThrow(() -> new CustomException(ErrorCode.LIKE_NOT_FOUND));
    }

    boolean existsByNewsFeedIdAndUserId(Long newsFeedId, Long userId);

    Long user(UserEntity user);
}
