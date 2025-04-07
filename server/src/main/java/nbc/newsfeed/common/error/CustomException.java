package nbc.newsfeed.common.error;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

    private final ErrorCode errorCode;

    public CustomException(ErrorCode errorCode) {
        super(errorCode.getMessage()); // ✅ 예외 메시지를 부모(RuntimeException)에 전달
        this.errorCode = errorCode;
    }
}