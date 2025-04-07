package nbc.newsfeed.common.error;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {

	// ✅ 인증 로직 관련 에러
	AUTH_UNAUTHORIZED("인증이 필요한 요청입니다", HttpStatus.UNAUTHORIZED),
	AUTH_TOKEN_EXPIRED("만료된 토큰입니다.", HttpStatus.UNAUTHORIZED),
	FORBIDDEN("권한 없는 유저입니다.", HttpStatus.FORBIDDEN),
	// ✅ 유저 로직 관련 에러
	USER_NOT_FOUND("존재하지 않는 사용자입니다", HttpStatus.UNAUTHORIZED),
	PASSWORD_MISMATCH("비밀번호가 일치하지 않습니다", HttpStatus.UNAUTHORIZED),
	DUPLICATED_EMAIL("이미 등록된 이메일입니다.", HttpStatus.CONFLICT),
	SAME_PASSWORD("이전 비밀번호와 동일한 비밀번호로 변경할 수 없습니다.", HttpStatus.BAD_REQUEST),

	// ✅ 뉴스피드 관련 에러
	SCHEDULE_NOT_FOUND("존재하지 않는 일정입니다", HttpStatus.NOT_FOUND),

	// ✅ 댓글 관련 에러
	COMMENT_NOT_FOUND("존재하지 않는 댓글입니다", HttpStatus.NOT_FOUND),
	UNAUTHORIZED("해당 댓글에 대한 권한이 없습니다", HttpStatus.UNAUTHORIZED);

	private final String message;
	private final HttpStatus status;
}