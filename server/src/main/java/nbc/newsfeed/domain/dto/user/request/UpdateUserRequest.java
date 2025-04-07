package nbc.newsfeed.domain.dto.user.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UpdateUserRequest(
	@Pattern(
		regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]).{8,20}$",
		message = "비밀번호는 영문 대소문자, 숫자, 특수문자를 포함해 8자 이상 20자 이하이어야 합니다."
	)
	String password,

	@Pattern(
		regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]).{8,20}$",
		message = "비밀번호는 영문 대소문자, 숫자, 특수문자를 포함해 8자 이상 20자 이하이어야 합니다."
	)
	String updatedPassword,

	@NotBlank(message = "닉네임은 필수 입력 값입니다")
	@Size(min = 2, max = 10, message = "닉네임은 8자 이상, 20자 이하 여야 합니다")
	String nickname
) {

}