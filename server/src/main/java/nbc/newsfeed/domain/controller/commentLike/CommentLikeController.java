package nbc.newsfeed.domain.controller.commentLike;

import lombok.RequiredArgsConstructor;
import nbc.newsfeed.domain.service.commentLike.CommentLikeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class CommentLikeController {
    private final CommentLikeService commentLikeService;

    @PostMapping("/{commentId}/like")
    public ResponseEntity<Void> toggleLike(
            @PathVariable Long commentId,
            @RequestParam Long userId
    ) {
        commentLikeService.toggleLike(commentId, userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{commentId}/likes")
    public ResponseEntity<Integer> getLikeCount(
            @PathVariable Long commentId
    ) {
        int count = commentLikeService.getLikeCount(commentId);
        return ResponseEntity.ok(count);
    }
}