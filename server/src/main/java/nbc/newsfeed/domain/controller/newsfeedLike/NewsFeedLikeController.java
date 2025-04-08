package nbc.newsfeed.domain.controller.newsfeedLike;

import lombok.RequiredArgsConstructor;
import nbc.newsfeed.domain.dto.newsfeedLike.LikeUserResponseDto;
import nbc.newsfeed.domain.service.newsfeedLike.NewsFeedLikeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/news")
public class NewsFeedLikeController {
    private final NewsFeedLikeService newsFeedLikeService;

    @PostMapping("/{newsId}/like")
    public ResponseEntity<Void> toggleLike(
            @PathVariable Long newsId,
            Authentication authentication
    ) {
        long userId = Long.parseLong(authentication.getName());
        newsFeedLikeService.toggleLike(newsId, userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{newsId}/likes")
    public ResponseEntity<Integer> getLikeCount(
            @PathVariable Long newsId
    ) {
        int count = newsFeedLikeService.getLikeCount(newsId);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/{newsId}/like-users")
    public ResponseEntity<List<LikeUserResponseDto>> getLikeUsers(
            @PathVariable Long newsId
    ) {
        List<LikeUserResponseDto> likeUsers = newsFeedLikeService.getLikeUsers(newsId);
        return new ResponseEntity<>(likeUsers, HttpStatus.OK);
    }
}
