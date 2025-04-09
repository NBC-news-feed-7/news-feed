package nbc.newsfeed.domain.controller.commentLike;

import lombok.RequiredArgsConstructor;
import nbc.newsfeed.domain.dto.newsfeedLike.LikeUserResponseDto;
import nbc.newsfeed.domain.service.commentLike.CommentLikeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class CommentLikeController {
    private final CommentLikeService commentLikeService;

    @PostMapping("/{commentId}/like")
    public ResponseEntity<Void> addLike(
            @PathVariable Long commentId,
            Authentication authentication
    ) {
        long userId = Long.parseLong(authentication.getName());
        commentLikeService.addLike(commentId, userId);
        return ResponseEntity.ok().build();
    }
    @DeleteMapping("/{commentId}/likes")
    public ResponseEntity<Void> removeLike(
            @PathVariable Long commentId,
            Authentication authentication
    ) {
        Long userId = Long.parseLong(authentication.getName());
        commentLikeService.removeLike(commentId, userId);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/{commentId}/like-users")
    public ResponseEntity<List<LikeUserResponseDto>> getLikeUsers(
            @PathVariable Long commentId
    ) {
        List<LikeUserResponseDto> likeUsers = commentLikeService.getLikeUsers(commentId);
        return new ResponseEntity<>(likeUsers, HttpStatus.OK);
    }
}