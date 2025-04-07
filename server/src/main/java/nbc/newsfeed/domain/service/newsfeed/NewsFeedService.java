package nbc.newsfeed.domain.service.newsfeed;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nbc.newsfeed.common.error.CustomException;
import nbc.newsfeed.common.error.ErrorCode;
import nbc.newsfeed.domain.dto.newsfeeddto.NewsFeedResponseDto;
import nbc.newsfeed.domain.entity.NewsFeedEntity;
import nbc.newsfeed.domain.repository.newsfeed.NewsFeedRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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


}
