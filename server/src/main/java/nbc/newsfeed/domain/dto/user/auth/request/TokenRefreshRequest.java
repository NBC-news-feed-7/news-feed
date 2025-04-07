package nbc.newsfeed.domain.dto.user.auth.request;

import jakarta.validation.constraints.NotBlank;

public record TokenRefreshRequest(
	@NotBlank
	String refreshToken
) {
}