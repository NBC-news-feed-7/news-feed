package nbc.newsfeed.domain.dto.newsfeed;

import lombok.Builder;
import lombok.Getter;
import nbc.newsfeed.domain.dto.comment.response.CommentResponseDTO;

import java.util.List;

@Builder
@Getter
public class NewsFeedDetailResponseDto {
    private NewsFeedDto newsFeedDto;
    private Long likeCount;
    private List<CommentResponseDTO> commentDtoList;

    public static NewsFeedDetailResponseDto fromDto(NewsFeedDto newsFeedDto, int likeCount, List<CommentResponseDTO> listDto){
        return NewsFeedDetailResponseDto.builder()
                .newsFeedDto(newsFeedDto)
                .commentDtoList(listDto)
                .likeCount((long)likeCount).build();
    }
}
