package nbc.newsfeed.domain.dto.comment.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class CommentResponseDTO {
    private Long id;
    private String nickname;
    private String content;
    private Long parentCommentId;
    private Long newsFeedId;
    private int likeCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
