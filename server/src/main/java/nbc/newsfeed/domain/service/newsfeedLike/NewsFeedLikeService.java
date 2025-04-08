package nbc.newsfeed.domain.service.newsfeedLike;

import lombok.RequiredArgsConstructor;
import nbc.newsfeed.common.error.CustomException;
import nbc.newsfeed.common.error.ErrorCode;
import nbc.newsfeed.domain.entity.NewsFeedEntity;
import nbc.newsfeed.domain.entity.UserEntity;
import nbc.newsfeed.domain.repository.newsfeed.NewsFeedRepository;
import nbc.newsfeed.domain.repository.newsfeedLike.NewsFeedLikeRepository;
import nbc.newsfeed.domain.repository.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NewsFeedLikeService {
    private final NewsFeedLikeRepository likeRepository;
    private final NewsFeedRepository newsFeedRepository;
    private final UserRepository userRepository;

    public void toggleLike(Long newsId, Long userId) {
        NewsFeedEntity newsFeed = newsFeedRepository.findById(newsId)
                .orElseThrow(() -> new CustomException(ErrorCode.NEWSFEED_NOT_FOUND));
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        newsFeed.toggleLike(user, likeRepository);
    }

    @Transactional(readOnly = true)
    public int getLikeCount(Long newsId) {
        Optional<NewsFeedEntity> newsFeed  = Optional.ofNullable(newsFeedRepository.findById(newsId)
                .orElseThrow(() -> new CustomException(ErrorCode.NEWSFEED_NOT_FOUND)));

        return likeRepository.countByNewsFeed(newsFeed);
    }
}
