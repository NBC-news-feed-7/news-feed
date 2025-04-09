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
@RequestMapping("/api/feeds")
public class NewsFeedLikeController {
    private final NewsFeedLikeService newsFeedLikeService;

    @PostMapping("/{newsId}/likes")
    public ResponseEntity<Void> addLike(
            @PathVariable Long newsId,
            Authentication authentication
    ) {
        Long userId = Long.parseLong(authentication.getName());
        newsFeedLikeService.addLike(newsId, userId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{newsId}/likes")
    public ResponseEntity<Void> removeLike(
            @PathVariable Long newsId,
            Authentication authentication
    ) {
        Long userId = Long.parseLong(authentication.getName());
        newsFeedLikeService.removeLike(newsId, userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{newsId}/like-users")
    public ResponseEntity<List<LikeUserResponseDto>> getLikeUsers(
            @PathVariable Long newsId
    ) {
        List<LikeUserResponseDto> likeUsers = newsFeedLikeService.getLikeUsers(newsId);
        return new ResponseEntity<>(likeUsers, HttpStatus.OK);
    }
}
