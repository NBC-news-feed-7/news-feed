package nbc.newsfeed.domain.dto.comment.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    @Builder.Default
    private List<CommentResponseDTO> children = new ArrayList<>();


}
