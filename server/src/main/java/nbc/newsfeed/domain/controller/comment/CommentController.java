package nbc.newsfeed.domain.controller.comment;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nbc.newsfeed.domain.dto.comment.request.CreateCommentRequestDTO;
import nbc.newsfeed.domain.dto.comment.request.UpdateCommentRequestDTO;
import nbc.newsfeed.domain.dto.comment.response.CommentResponseDTO;
import nbc.newsfeed.domain.dto.comment.response.PutCommentResponseDTO;
import nbc.newsfeed.domain.service.comment.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class CommentController {
    private final CommentService commentService;
    @GetMapping
    public List<CommentResponseDTO> getComments(
            @RequestParam Long feedId
    ){
        List<CommentResponseDTO> commentResponses = commentService.getCommentsByNewsFeedId(feedId);
        return commentResponses;
    }
    @PostMapping
    public ResponseEntity<CommentResponseDTO> createComment(
            Authentication authentication,
            @Valid @RequestBody CreateCommentRequestDTO createCommentRequestDTO
    ){
        Long userId = Long.parseLong(authentication.getName());
        CommentResponseDTO commentResponseDTO = commentService.createComment(userId, createCommentRequestDTO);
        return ResponseEntity.ok(commentResponseDTO);
    }
    @PutMapping
    public ResponseEntity<PutCommentResponseDTO> updateComment(
            Authentication authentication,
            @Valid @RequestBody UpdateCommentRequestDTO updateCommentRequestDTO
    ){
        Long userId = Long.parseLong(authentication.getName());
        PutCommentResponseDTO updatedComment = commentService.updateComment(userId, updateCommentRequestDTO);
        System.out.println(updatedComment);
        return ResponseEntity.ok(updatedComment);
    }
    @DeleteMapping
    public ResponseEntity<Void> deleteComment(
            Authentication authentication,
            @RequestParam Long commentId
    ){
        Long userId = Long.parseLong(authentication.getName());
        // 소프트 딜리트
        commentService.deleteComment(commentId,userId);
        return ResponseEntity.noContent().build();
    }
}
