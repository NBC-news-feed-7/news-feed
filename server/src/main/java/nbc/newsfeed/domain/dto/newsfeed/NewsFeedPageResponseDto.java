package nbc.newsfeed.domain.dto.newsfeed;

import lombok.Builder;
import lombok.Getter;
import nbc.newsfeed.domain.entity.NewsFeedEntity;

import java.time.LocalDateTime;

@Getter
@Builder
public class NewsFeedPageResponseDto {
    private Long feedId;
    private Long userId;
    private String nickName;
    private String title;
    private String content;
    private LocalDateTime updatedAt;


    public static NewsFeedPageResponseDto ofPageResponse(NewsFeedEntity entity) {
        return NewsFeedPageResponseDto.builder()
                .feedId(entity.getId())
                .userId(entity.getUser().getId())
                .nickName(entity.getUser().getNickname())
                .title(entity.getTitle())
                .content(entity.getContent())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
