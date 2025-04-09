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
    private List<String> pathList;

    public static NewsFeedDetailResponseDto fromDto(NewsFeedDto newsFeedDto, int likeCount, List<CommentResponseDTO> listDto, List<String> pathList){
        return NewsFeedDetailResponseDto.builder()
                .newsFeedDto(newsFeedDto)
                .pathList(pathList)
                .commentDtoList(listDto)
                .likeCount((long)likeCount).build();
    }
}
