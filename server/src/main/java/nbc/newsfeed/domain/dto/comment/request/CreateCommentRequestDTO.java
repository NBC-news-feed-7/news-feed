package nbc.newsfeed.domain.dto.comment.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateCommentRequestDTO {
    @NotNull
    private Long feedId;
    @Size(min = 1, max = 100, message = "최대 100자까지 입력해주세요.")
    private String content;
    private Long parentCommentId;


}
