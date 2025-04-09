package nbc.newsfeed.domain.controller.newsfeed;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nbc.newsfeed.domain.dto.comment.response.CommentResponseDTO;
import nbc.newsfeed.domain.dto.newsfeed.*;
import nbc.newsfeed.domain.service.comment.CommentService;
import nbc.newsfeed.domain.service.newsfeed.NewsFeedService;
import nbc.newsfeed.domain.service.newsfeedLike.NewsFeedLikeService;
import nbc.newsfeed.domain.dto.newsfeed.NewsFeedSortType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    private final NewsFeedLikeService newsFeedLikeService;
    private final CommentService commentService;

    @GetMapping("/{feedsId}")
    public ResponseEntity<NewsFeedDetailResponseDto> getNewsFeed(
            @PathVariable Long feedsId,
            Authentication authentication
    ) {
        NewsFeedDto newsFeedDto = newsFeedService.getNewsFeed(feedsId);
        int likeCount = newsFeedLikeService.getLikeCount(feedsId);
        List<CommentResponseDTO> commentResponseDTOList = commentService.getCommentsByNewsFeedId(feedsId);
        NewsFeedDetailResponseDto responseDto = NewsFeedDetailResponseDto.fromDto(newsFeedDto, likeCount, commentResponseDTOList);
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping
    public ResponseEntity<NewsFeedDto> createNewsFeed(
            Authentication authentication,
            @Valid @RequestBody NewsFeedRequestDto requestDto
    ) {
        Long userId = Long.parseLong(authentication.getName());
        NewsFeedDto responseDto = newsFeedService.createNewsFeed(userId, requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @PutMapping("/{feedsId}")
    public ResponseEntity<NewsFeedDto> updateNewsFeed(
            @PathVariable Long feedsId,
            Authentication authentication,
            @Valid @RequestBody NewsFeedRequestDto requestDto
    ) {
        Long userId = Long.parseLong(authentication.getName());
        NewsFeedDto responseDto = newsFeedService.updateNewsFeed(userId, feedsId, requestDto);
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
    public ResponseEntity<Page<NewsFeedPageResponseDto>> getFeedsByKeyword(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "LATEST") NewsFeedSortType sortType,
            Pageable pageable
    ) {
        Page<NewsFeedPageResponseDto> feeds = newsFeedService.getFeedsByKeyword(keyword, sortType, pageable);
        return ResponseEntity.ok(feeds);
    }
}
