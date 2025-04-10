package nbc.newsfeed.common.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import nbc.newsfeed.domain.service.auth.model.Token;

/**
 * @author    : kimjungmin
 * Created on : 2025. 4. 9.
 */
public final class AuthCookieUtil {
	public static final String ACCESS_TOKEN_COOKIE = "accessToken";
	public static final String REFRESH_TOKEN_COOKIE = "refreshToken";

	// 쿠키에 AccessToken, RefreshToken Setting
	public static void addAuthCookies(HttpServletResponse response, Token token) {
		Cookie accessTokenCookie = new Cookie(ACCESS_TOKEN_COOKIE, token.getAccessToken());
		accessTokenCookie.setHttpOnly(true);
		accessTokenCookie.setPath("/");
		accessTokenCookie.setMaxAge(
			(int)((token.getAccessTokenExpiredAt().getTime() - System.currentTimeMillis()) / 1000));

		Cookie refreshTokenCookie = new Cookie(REFRESH_TOKEN_COOKIE, token.getRefreshToken());
		refreshTokenCookie.setHttpOnly(true);
		refreshTokenCookie.setPath("/");
		refreshTokenCookie.setMaxAge(
			(int)((token.getRefreshTokenExpiredAt().getTime() - System.currentTimeMillis()) / 1000));

		response.addCookie(accessTokenCookie);
		response.addCookie(refreshTokenCookie);
	}
}
