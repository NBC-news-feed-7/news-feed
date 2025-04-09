package nbc.newsfeed.domain.dto.newsfeed;

import lombok.Builder;
import lombok.Getter;


import java.util.List;
@Getter
@Builder
public class NewsFeedWithImagesDto {
    private NewsFeedDto newsFeedDto;
    private List<String> pathList;

    public static NewsFeedWithImagesDto fromEntity(NewsFeedDto newsFeedDto, List<String> pathList) {
        return NewsFeedWithImagesDto.builder()
                .newsFeedDto(newsFeedDto)
                .pathList(pathList)
                .build();
    }
}
