package nbc.newsfeed.common.error;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {

    // ✅ 유저 로직 관련 에러
    USER_NOT_FOUND( "존재하지 않는 사용자입니다", HttpStatus.UNAUTHORIZED),
    PASSWORD_MISMATCH( "비밀번호가 일치하지 않습니다", HttpStatus.UNAUTHORIZED),
    DUPLICATED_EMAIL( "이미 등록된 이메일입니다.", HttpStatus.CONFLICT),

    // ✅ 뉴스피드 관련 에러
    SCHEDULE_NOT_FOUND( "존재하지 않는 일정입니다", HttpStatus.NOT_FOUND),

    // ✅ 댓글 관련 에러
    COMMENT_NOT_FOUND( "존재하지 않는 댓글입니다", HttpStatus.NOT_FOUND),
    UNAUTHORIZED( "해당 댓글에 대한 권한이 없습니다", HttpStatus.UNAUTHORIZED),

    // ✅ 친구 요청 관련 에러
    REQUEST_NOT_FOUND( "존재하지 않는 친구 요청입니다", HttpStatus.NOT_FOUND),
    ALREADY_REQUESTED( "이미 친구 요청을 보냈습니다", HttpStatus.CONFLICT),
    INVALID_STATUS( "유효하지 않은 친구 요청 상태입니다", HttpStatus.BAD_REQUEST);

    private final String message;
    private final HttpStatus status;
}