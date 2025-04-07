package nbc.newsfeed.domain.dto.auth.response;

import java.util.Date;

import nbc.newsfeed.domain.service.auth.model.Token;

public record TokenResponse(
	String accessToken,
	Date accessTokenIssuedAt,
	Date accessTokenExpiredAt,
	String refreshToken,
	Date refreshTokenIssuedAt,
	Date refreshTokenExpiredAt
) {

	public static TokenResponse from(Token token) {
		return new TokenResponse(
			token.getAccessToken(),
			token.getAccessTokenIssuedAt(),
			token.getAccessTokenExpiredAt(),
			token.getRefreshToken(),
			token.getRefreshTokenIssuedAt(),
			token.getRefreshTokenExpiredAt()
		);
	}
}
