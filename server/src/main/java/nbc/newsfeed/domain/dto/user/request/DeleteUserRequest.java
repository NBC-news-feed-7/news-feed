package nbc.newsfeed.domain.dto.user.request;

import jakarta.validation.constraints.Pattern;

public record DeleteUserRequest(
	@Pattern(
		regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]).{8,20}$",
		message = "비밀번호는 영문 대소문자, 숫자, 특수문자를 포함해 8자 이상 20자 이하이어야 합니다."
	)
	String password
) {

}