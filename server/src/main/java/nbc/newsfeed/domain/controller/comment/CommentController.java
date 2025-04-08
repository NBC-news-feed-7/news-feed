package nbc.newsfeed.domain.controller.comment;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import nbc.newsfeed.domain.dto.comment.response.CommentResponse;
import nbc.newsfeed.domain.service.comment.CommentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class CommentController {
    private final CommentService commentService;
    @GetMapping
    public List<CommentResponse> getComments(
            @RequestParam Long id
    ){
        List<CommentResponse> commentResponses = commentService.getCommentsByNewsFeedId(id);
        return null;
    }
}
