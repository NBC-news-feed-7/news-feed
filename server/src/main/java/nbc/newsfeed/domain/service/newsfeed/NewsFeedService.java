package nbc.newsfeed.domain.service.newsfeed;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nbc.newsfeed.common.error.CustomException;
import nbc.newsfeed.common.error.ErrorCode;
import nbc.newsfeed.domain.dto.newsfeeddto.NewsFeedRequestDto;
import nbc.newsfeed.domain.dto.newsfeeddto.NewsFeedResponseDto;
import nbc.newsfeed.domain.dto.newsfeeddto.NewsFeedSortType;
import nbc.newsfeed.domain.entity.NewsFeedEntity;
import nbc.newsfeed.domain.entity.UserEntity;
import nbc.newsfeed.domain.repository.newsfeed.NewsFeedRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NewsFeedService {

    private final NewsFeedRepository newsFeedRepository;

    public NewsFeedResponseDto getNewsFeed(Long feedsId) {

        NewsFeedEntity newsFeedEntity = newsFeedRepository.findById(feedsId)
                .orElseThrow(() -> new CustomException(ErrorCode.NEWSFEED_NOT_FOUND));
        //comment, newsfeedLike 수 가져오기 해야댐

        NewsFeedResponseDto responseDto = NewsFeedResponseDto.fromEntity(newsFeedEntity, 0L, 0L);
        return responseDto;
    }

    public NewsFeedResponseDto createNewsFeed(Long userId, NewsFeedRequestDto requestDto) {
        //userRepository에서 유저 찾아야댐
        UserEntity findUser = UserEntity.builder()
                .nickname("test")
                .email("test")
                .password("test")
                .id(userId).build();
        NewsFeedEntity createNewsFeedEntity = NewsFeedEntity.builder()
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .user(findUser).build();
        NewsFeedEntity savedNewsFeed = newsFeedRepository.save(createNewsFeedEntity);
        //comment, newsfeedLike 수 가져오기 해야댐
        return NewsFeedResponseDto.fromEntity(savedNewsFeed, 0L, 0L);
    }

    public NewsFeedResponseDto updateNewsFeed(Long userId, NewsFeedRequestDto requestDto) {

        //유저찾는거 해야댐
        UserEntity findUser = UserEntity.builder()
                .nickname("test")
                .email("test")
                .password("test")
                .id(userId).build();
        //업데이트로직 작성해야댐
        NewsFeedEntity createNewsFeedEntity = NewsFeedEntity.builder()
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .user(findUser).build();
        NewsFeedEntity savedNewsFeed = newsFeedRepository.save(createNewsFeedEntity);
        //comment, newsfeedLike 수 가져오기 해야댐
        return NewsFeedResponseDto.fromEntity(savedNewsFeed, 0L, 0L);
    }

    public void deleteNewsFeed(Long userId, Long feedsId) {

        NewsFeedEntity findNewsFeed = newsFeedRepository.findById(feedsId)
                .orElseThrow(() -> new CustomException(ErrorCode.NEWSFEED_NOT_FOUND));
        if (!findNewsFeed.getUser().getId().equals(userId)) {
            throw new CustomException(ErrorCode.NEWSFEED_FORBIDDEN);
        }
        newsFeedRepository.delete(findNewsFeed);
    }

    @Transactional(readOnly = true)
    public List<NewsFeedResponseDto> getFeedsBySort(NewsFeedSortType sortType) {
        return newsFeedRepository.findFeedsWithSort(sortType);
    }

}
