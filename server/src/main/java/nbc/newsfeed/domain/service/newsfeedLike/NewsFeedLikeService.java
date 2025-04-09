package nbc.newsfeed.domain.service.newsfeedLike;

import lombok.RequiredArgsConstructor;
import nbc.newsfeed.common.error.CustomException;
import nbc.newsfeed.common.error.ErrorCode;
import nbc.newsfeed.domain.dto.newsfeedLike.LikeUserResponseDto;
import nbc.newsfeed.domain.entity.NewsFeedEntity;
import nbc.newsfeed.domain.entity.NewsFeedLikeEntity;
import nbc.newsfeed.domain.entity.UserEntity;
import nbc.newsfeed.domain.repository.newsfeed.NewsFeedRepository;
import nbc.newsfeed.domain.repository.newsfeedLike.NewsFeedLikeRepository;
import nbc.newsfeed.domain.repository.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NewsFeedLikeService {
    private final NewsFeedLikeRepository likeRepository;
    private final NewsFeedRepository newsFeedRepository;
    private final UserRepository userRepository;
    private final NewsFeedLikeRepository newsFeedLikeRepository;

    public void addLike(Long newsId, Long userId) {
        NewsFeedEntity newsFeed = newsFeedRepository.getByIdOrThrow(newsId);
        UserEntity user = userRepository.getByIdOrThrow(userId);

        newsFeedLikeRepository.validateNotAlreadyLiked(newsFeed, user);

        NewsFeedLikeEntity like = NewsFeedLikeEntity.of(newsFeed, user);
        newsFeedLikeRepository.save(like);
    }

    public void removeLike(Long newsId, Long userId) {
        NewsFeedEntity newsFeed = newsFeedRepository.getByIdOrThrow(newsId);
        UserEntity user = userRepository.getByIdOrThrow(userId);

        NewsFeedLikeEntity like = newsFeedLikeRepository.getByNewsFeedAndUserOrThrow(newsFeed, user);

        newsFeedLikeRepository.delete(like);
    }

    @Transactional(readOnly = true)
    public int getLikeCount(Long newsId) {
        Optional<NewsFeedEntity> newsFeed  = Optional.ofNullable(newsFeedRepository.findById(newsId)
                .orElseThrow(() -> new CustomException(ErrorCode.NEWSFEED_NOT_FOUND)));

        return likeRepository.countByNewsFeed(newsFeed);
    }

    public List<LikeUserResponseDto> getLikeUsers(Long newsId) {
        return likeRepository.findLikeUsersByNewsId(newsId);
    }

}
