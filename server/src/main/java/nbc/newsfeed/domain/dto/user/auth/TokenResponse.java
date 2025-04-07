package nbc.newsfeed.domain.dto.user.auth;

import java.util.Date;

import nbc.newsfeed.domain.service.auth.model.Token;

public record TokenResponse(
	String accessToken,
	String refreshToken,
	Date issuedAt,
	Date expiredAt
) {

	public static TokenResponse from(Token token) {
		return new TokenResponse(
			token.getAccessToken(),
			token.getRefreshToken(),
			token.getIssuedAt(),
			token.getExpiredAt()
		);
	}
}
