package nbc.newsfeed.domain.dto.newsfeed;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
public class NewsFeedRequestDto{
    @NotBlank(message = "제목은 비어 있을 수 없습니다.")
    @Size(min = 3, max = 20, message = "제목은 3자 이상 20자 이하로 입력해주세요.")
    private String title;

    @NotBlank(message = "내용은 비어 있을 수 없습니다.")
    @Size(max = 255, message = "내용은 255자 이하로 입력해주세요.")
    private String content;
}
