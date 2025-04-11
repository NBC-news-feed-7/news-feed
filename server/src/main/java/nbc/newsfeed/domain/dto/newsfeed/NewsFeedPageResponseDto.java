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
    private String thumbnailUrl;
    private Long viewCount;

    private static final String BASE_URL = "http://localhost:8080/";
    private static final String DEFAULT_THUMBNAIL = "https://androidguias.com/wp-content/uploads/2021/10/thumbnail-2-1024x512.jpg.webp";

    @QueryProjection
    public NewsFeedPageResponseDto(Long feedId, Long userId, String nickName,
                                   String title, String content,
                                   LocalDateTime updatedAt,
                                   Long likeCount, Long commentCount,
                                   String thumbnailPath, Long viewCount) {
        this.feedId = feedId;
        this.userId = userId;
        this.nickName = nickName;
        this.title = title;
        this.content = content;
        this.updatedAt = updatedAt;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
        this.thumbnailUrl = (thumbnailPath != null)
                ? BASE_URL + thumbnailPath.replace("\\", "/")
                : DEFAULT_THUMBNAIL;
        this.viewCount = viewCount;
    }
}

