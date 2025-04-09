package nbc.newsfeed.common.error;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FieldErrorDto {
    private String field;
    private String message;
}
