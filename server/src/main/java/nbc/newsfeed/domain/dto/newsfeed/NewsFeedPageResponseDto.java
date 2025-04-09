package nbc.newsfeed.domain.dto.newsfeed;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class NewsFeedPageResponseDto {
    private Long feedId;
    private Long userId;
    private String nickName;
    private String title;
    private String content;
    private LocalDateTime updatedAt;
    private Long likeCount;
    private Long commentCount;

    @QueryProjection
    public NewsFeedPageResponseDto(Long feedId, Long userId, String nickName,
                                   String title, String content,
                                   LocalDateTime updatedAt,
                                   Long likeCount, Long commentCount) {
        this.feedId = feedId;
        this.userId = userId;
        this.nickName = nickName;
        this.title = title;
        this.content = content;
        this.updatedAt = updatedAt;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
    }
}
