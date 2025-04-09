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
	EMPTY_PROFILE_IMAGE("프로필 이미지가 비어 있습니다!", HttpStatus.BAD_REQUEST),
	IMAGE_SAVE_FAIL("이미지 저장에 실패 했습니다!", HttpStatus.INTERNAL_SERVER_ERROR),

	// ✅ 뉴스피드 관련 에러
	NEWSFEED_NOT_FOUND("존재하지 않는 피드입니다", HttpStatus.NOT_FOUND),
	NEWSFEED_FORBIDDEN("해당 피드에 대한 권한이 없습니다", HttpStatus.NOT_FOUND),

	// ✅ 댓글 관련 에러
	COMMENT_NOT_FOUND("존재하지 않는 댓글입니다", HttpStatus.NOT_FOUND),
	UNAUTHORIZED("해당 댓글에 대한 권한이 없습니다", HttpStatus.UNAUTHORIZED),

	// ✅ 친구 요청 관련 에러
	REQUEST_NOT_FOUND("존재하지 않는 친구 요청입니다", HttpStatus.NOT_FOUND),
	ALREADY_REQUESTED("이미 친구 요청을 보냈습니다", HttpStatus.CONFLICT),
	INVALID_STATUS("유효하지 않은 친구 요청 상태입니다", HttpStatus.BAD_REQUEST),
	ALREADY_FRIEND("이미 친구인 상태입니다", HttpStatus.CONFLICT),
	INVALID_CANCEL("취소할 수 없는 친구 요청 상태입니다", HttpStatus.BAD_REQUEST),

	// ✅ 친구 요청 관련 에러
	ALREADY_LIKED("이미 좋아요를 누르셨습니다.",HttpStatus.CONFLICT ),
	LIKE_NOT_FOUND("좋아요 정보가 존재하지 않습니다.",HttpStatus.NOT_FOUND),
	NOT_LIKED_YET("좋아요를 누르지 않았습니다.",HttpStatus.NOT_FOUND );

	private final String message;
	private final HttpStatus status;
}