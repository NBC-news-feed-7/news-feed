package nbc.newsfeed.domain.dto.newsfeeddto;


import lombok.Builder;
import lombok.Getter;
import nbc.newsfeed.domain.entity.NewsFeedEntity;

import java.time.LocalDateTime;

@Getter
@Builder
public class NewsFeedResponseDto {

    private Long feedId;
    private Long userId;
    private String nickName;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long likeCount;
    private Long commentCount;

    public static NewsFeedResponseDto fromEntity(NewsFeedEntity entity, Long likeCount, Long commentCount) {
        return NewsFeedResponseDto.builder()
                .feedId(entity.getId())
                .userId(entity.getUser().getId())
                .nickName(entity.getUser().getNickname())
                .title(entity.getTitle())
                .content(entity.getContent())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .likeCount(likeCount)
                .commentCount(commentCount)
                .build();
    }

}
