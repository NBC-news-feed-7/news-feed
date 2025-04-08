package nbc.newsfeed.domain.controller.newsfeed;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nbc.newsfeed.domain.dto.newsfeeddto.NewsFeedRequestDto;
import nbc.newsfeed.domain.dto.newsfeeddto.NewsFeedResponseDto;
import nbc.newsfeed.domain.dto.newsfeeddto.NewsFeedSortType;
import nbc.newsfeed.domain.service.newsfeed.NewsFeedService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/feeds")
@RequiredArgsConstructor
public class NewsFeedController {

    private final NewsFeedService newsFeedService;

    @GetMapping("/{feedsId}")
    public ResponseEntity<NewsFeedResponseDto> getNewsFeed(
            @PathVariable Long feedsId
    ) {
        NewsFeedResponseDto responseDto = newsFeedService.getNewsFeed(feedsId);
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping
    public ResponseEntity<NewsFeedResponseDto> createNewsFeed(
            Authentication authentication,
            @RequestBody NewsFeedRequestDto requestDto
    ) {
        Long userId = Long.parseLong(authentication.getName());
        NewsFeedResponseDto responseDto = newsFeedService.createNewsFeed(userId, requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @PutMapping("/{feedsId}")
    public ResponseEntity<NewsFeedResponseDto> updateNewsFeed(
            Authentication authentication,
            @RequestBody NewsFeedRequestDto requestDto
    ) {
        Long userId = Long.parseLong(authentication.getName());
        NewsFeedResponseDto responseDto = newsFeedService.updateNewsFeed(userId, requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{feedsId}")
    public ResponseEntity<Void> deleteNewsFeed(
            @PathVariable Long feedsId,
            Authentication authentication
    ) {
        Long userId = Long.parseLong(authentication.getName());
        newsFeedService.deleteNewsFeed(userId, feedsId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping
    public ResponseEntity<List<NewsFeedResponseDto>> getNewsFeedsBySort(
            @RequestParam(defaultValue = "LATEST") String sort
    ) {
        NewsFeedSortType sortType = NewsFeedSortType.valueOf(sort.toUpperCase());
        List<NewsFeedResponseDto> result = newsFeedService.getFeedsBySort(sortType);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
