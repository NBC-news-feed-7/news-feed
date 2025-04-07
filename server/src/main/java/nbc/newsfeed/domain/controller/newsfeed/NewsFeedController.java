package nbc.newsfeed.domain.controller.newsfeed;


import lombok.RequiredArgsConstructor;
import nbc.newsfeed.domain.dto.newsfeeddto.NewsFeedRequestDto;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import lombok.extern.slf4j.Slf4j;
import nbc.newsfeed.domain.dto.newsfeeddto.NewsFeedResponseDto;
import nbc.newsfeed.domain.service.newsfeed.NewsFeedService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/feeds")
@RequiredArgsConstructor
public class NewsFeedController {

    private final NewsFeedService newsFeedService;

    @GetMapping("/{feedsId}")
    public ResponseEntity<NewsFeedResponseDto> getNewsFeed(
            @PathVariable Long feedsId
    ){
        NewsFeedResponseDto responseDto = newsFeedService.getNewsFeed(feedsId);
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping
    public ResponseEntity<NewsFeedResponseDto> createNewsFeed(
            Authentication authentication,
            @RequestBody NewsFeedRequestDto requestDto
            ){
        Long userId = Long.parseLong(authentication.getName());
        NewsFeedResponseDto responseDto = newsFeedService.createNewsFeed(userId, requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @PutMapping("/{feedsId}")
    public ResponseEntity<NewsFeedResponseDto> updateNewsFeed(
        Authentication authentication,
        @RequestBody NewsFeedRequestDto requestDto
    ){
        Long userId = Long.parseLong(authentication.getName());
        NewsFeedResponseDto responseDto = newsFeedService.updateNewsFeed(userId, requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{feedsId}")
    public ResponseEntity<Void> deleteNewsFeed(
            @PathVariable Long feedsId,
            Authentication authentication
    ){
        Long userId = Long.parseLong(authentication.getName());
        newsFeedService.deleteNewsFeed(userId, feedsId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
