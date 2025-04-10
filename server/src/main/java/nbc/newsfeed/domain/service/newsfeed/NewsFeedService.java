package nbc.newsfeed.domain.service.newsfeed;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nbc.newsfeed.common.error.CustomException;
import nbc.newsfeed.common.error.ErrorCode;
import nbc.newsfeed.domain.dto.newsfeed.NewsFeedDto;
import nbc.newsfeed.domain.dto.newsfeed.NewsFeedPageResponseDto;
import nbc.newsfeed.domain.dto.newsfeed.NewsFeedRequestDto;
import nbc.newsfeed.domain.dto.newsfeed.NewsFeedSortType;
import nbc.newsfeed.domain.entity.NewsFeedEntity;
import nbc.newsfeed.domain.entity.NewsFileEntity;
import nbc.newsfeed.domain.entity.UserEntity;
import nbc.newsfeed.domain.repository.newsfeed.NewsFeedRepository;
import nbc.newsfeed.domain.repository.newsfile.NewsFileRepository;
import nbc.newsfeed.domain.repository.friendrequest.FriendRequestRepository;
import nbc.newsfeed.domain.repository.user.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NewsFeedService {

    private final NewsFeedRepository newsFeedRepository;
    private final UserRepository userRepository;
    private final NewsFileRepository newsFileRepository;
    private final FriendRequestRepository friendRequestRepository;

    @Transactional(readOnly = true)
    public NewsFeedDto getNewsFeed(Long feedsId) {

        NewsFeedEntity newsFeedEntity = newsFeedRepository.findById(feedsId)
                .orElseThrow(() -> new CustomException(ErrorCode.NEWSFEED_NOT_FOUND));

        return NewsFeedDto.fromEntity(newsFeedEntity);
    }

    @Transactional(readOnly = true)
    public List<String> getNewsFeedImagesPaths(Long feedsID){
        return newsFileRepository.findAllByNewsFeed_Id(feedsID)
                .stream().map(NewsFileEntity::getPath)
                .toList();
    }

    @Transactional
    public NewsFeedDto createNewsFeed(Long userId, NewsFeedRequestDto requestDto) {
        //유저 찾기
        UserEntity findUser = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        //피드 엔티티 생성
        NewsFeedEntity createNewsFeedEntity = NewsFeedEntity.builder()
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .user(findUser).build();
        //피드 저장
        NewsFeedEntity savedNewsFeedEntity = newsFeedRepository.save(createNewsFeedEntity);

        return NewsFeedDto.fromEntity(savedNewsFeedEntity);
    }


    @Transactional
    public NewsFeedDto updateNewsFeed(Long userId, Long feedsId, NewsFeedRequestDto requestDto) {

        //유저찾기
        UserEntity findUser = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        //피드 찾기
        NewsFeedEntity findNewsFeedEntity = newsFeedRepository.findById(feedsId)
                .orElseThrow(() -> new CustomException(ErrorCode.NEWSFEED_NOT_FOUND));
        //권한 확인
        if (!findNewsFeedEntity.getUser().getId().equals(findUser.getId())) { //UserEntity equals 메서드 재정의 필요할수도
            throw new CustomException(ErrorCode.NEWSFEED_FORBIDDEN);
        }
        findNewsFeedEntity.update(requestDto);
        return NewsFeedDto.fromEntity(findNewsFeedEntity);
    }

    @Transactional
    public void deleteNewsFeed(Long userId, Long feedsId) {

        //피드찾기
        NewsFeedEntity findNewsFeed = newsFeedRepository.findById(feedsId)
                .orElseThrow(() -> new CustomException(ErrorCode.NEWSFEED_NOT_FOUND));
        //권한 확인
        if (!findNewsFeed.getUser().getId().equals(userId)) {
            throw new CustomException(ErrorCode.NEWSFEED_FORBIDDEN);
        }
        newsFeedRepository.delete(findNewsFeed);
    }

    @Transactional(readOnly = true)
    public Page<NewsFeedPageResponseDto> getFeedsByKeyword(String keyword, LocalDate startDate, LocalDate endDate, NewsFeedSortType sortType, Pageable pageable) {
        return newsFeedRepository.searchFeeds(keyword, startDate, endDate, sortType, pageable);
    }

    @Transactional(readOnly = true)
    public Page<NewsFeedPageResponseDto> getFriendFeeds(Long userId, NewsFeedSortType sortType, Pageable pageable) {

        // 친구 ID 목록 조회
        List<Long> friendIds = friendRequestRepository.findAllFriendIds(userId);

        return newsFeedRepository.searchFriendFeeds(friendIds, sortType, pageable);
    }
}
