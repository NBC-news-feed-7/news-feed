package nbc.newsfeed.domain.service.newsfeed;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nbc.newsfeed.common.error.CustomException;
import nbc.newsfeed.common.error.ErrorCode;
import nbc.newsfeed.domain.dto.newsfeed.NewsFeedRequestDto;
import nbc.newsfeed.domain.dto.newsfeed.NewsFeedDto;
import nbc.newsfeed.domain.entity.NewsFeedEntity;
import nbc.newsfeed.domain.entity.UserEntity;
import nbc.newsfeed.domain.repository.newsfeed.NewsFeedRepository;
import nbc.newsfeed.domain.repository.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class NewsFeedService {

    private final NewsFeedRepository newsFeedRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public NewsFeedDto getNewsFeed(Long feedsId) {

        NewsFeedEntity newsFeedEntity = newsFeedRepository.findById(feedsId)
                .orElseThrow(() -> new CustomException(ErrorCode.NEWSFEED_NOT_FOUND));
        //comment, newsfeedLike 수 가져오기 해야댐

        return NewsFeedDto.fromEntity(newsFeedEntity);
    }

    @Transactional
    public NewsFeedDto createNewsFeed(Long userId, NewsFeedRequestDto requestDto){
        //유저 찾기
        UserEntity findUser = userRepository.findById(userId)
                .orElseThrow(()->new CustomException(ErrorCode.USER_NOT_FOUND));
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
    public NewsFeedDto updateNewsFeed(Long userId, Long feedsId, NewsFeedRequestDto requestDto){

        //유저찾기
        UserEntity findUser = userRepository.findById(userId)
                .orElseThrow(()->new CustomException(ErrorCode.USER_NOT_FOUND));
        //피드 찾기
        NewsFeedEntity findNewsFeedEntity = newsFeedRepository.findById(feedsId)
                .orElseThrow(()->new CustomException(ErrorCode.NEWSFEED_NOT_FOUND));
        //권한 확인
        if(!findNewsFeedEntity.getUser().getId().equals(findUser.getId())){ //UserEntity equals 메서드 재정의 필요할수도
            throw new CustomException(ErrorCode.NEWSFEED_FORBIDDEN);
        }
        findNewsFeedEntity.update(requestDto);
        return NewsFeedDto.fromEntity(findNewsFeedEntity);
    }

    @Transactional
    public void deleteNewsFeed(Long userId, Long feedsId){

        //피드찾기
        NewsFeedEntity findNewsFeed = newsFeedRepository.findById(feedsId)
                                    .orElseThrow(()->new CustomException(ErrorCode.NEWSFEED_NOT_FOUND));
        //권한 확인
        if(!findNewsFeed.getUser().getId().equals(userId)){
            throw new CustomException(ErrorCode.NEWSFEED_FORBIDDEN);
        }
        newsFeedRepository.delete(findNewsFeed);
    }

    @Transactional(readOnly = true)
    public Page<NewsFeedResponseDto> getFeedsBySort(NewsFeedSortType sortType, Pageable pageable) {
        return newsFeedRepository.findFeedsWithSort(sortType, pageable);
    }

}
