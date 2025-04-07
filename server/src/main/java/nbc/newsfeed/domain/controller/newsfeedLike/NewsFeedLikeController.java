package nbc.newsfeed.domain.controller.newsfeedLike;

import lombok.RequiredArgsConstructor;
import nbc.newsfeed.domain.service.newsfeedLike.NewsFeedLikeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/news")
public class NewsFeedLikeController {
    private final NewsFeedLikeService newsFeedLikeService;

    @PostMapping("/{newsId}/like")
    public ResponseEntity<Void> toggleLike(
            @PathVariable Long newsId,
            @RequestParam Long userId
    ) {
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
}
