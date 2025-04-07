package nbc.newsfeed.domain.controller.newsfeed;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nbc.newsfeed.domain.dto.newsfeeddto.NewsFeedResponseDto;
import nbc.newsfeed.domain.service.newsfeed.NewsFeedService;
import org.springframework.http.HttpStatus;
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
        return ResponseEntity.ok().body(responseDto);
    }


}
