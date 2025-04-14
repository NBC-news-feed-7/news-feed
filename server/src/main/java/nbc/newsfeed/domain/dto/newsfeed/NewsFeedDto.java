package nbc.newsfeed.domain.dto.newsfeed;


import lombok.Builder;
import lombok.Getter;
import nbc.newsfeed.domain.entity.NewsFeedEntity;

import java.time.LocalDateTime;

@Getter
@Builder
public class NewsFeedDto {

    private Long feedId;
    private Long userId;
    private String nickName;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long viewCount;


    public static NewsFeedDto fromEntity(NewsFeedEntity entity) {
        //tes
        return NewsFeedDto.builder()
                .feedId(entity.getId())
                .userId(entity.getUser().getId())
                .nickName(entity.getUser().getNickname())
                .title(entity.getTitle())
                .content(entity.getContent())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .viewCount(entity.getViewCount())
                .build();
    }

}
