package nbc.newsfeed.domain.dto.newsfeed;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class NewsFeedDetailResponseDto {
    private NewsFeedDto newsFeedDto;
    private Long likeCount;
    //private List<> commentDtoList;

    public static NewsFeedDetailResponseDto fromDto(NewsFeedDto newsFeedDto, int likeCount){
        return NewsFeedDetailResponseDto.builder()
                .newsFeedDto(newsFeedDto)
                .likeCount((long)likeCount).build();
    }
}
