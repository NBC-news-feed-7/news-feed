package nbc.newsfeed.domain.controller.auth;

import static nbc.newsfeed.common.util.AuthCookieUtil.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nbc.newsfeed.domain.dto.auth.request.SigninRequest;
import nbc.newsfeed.domain.dto.auth.response.TokenResponse;
import nbc.newsfeed.domain.service.auth.AuthService;
import nbc.newsfeed.domain.service.auth.model.Token;

/**
 * @author    : kimjungmin
 * Created on : 2025. 4. 7.
 */
@RestController
@RequiredArgsConstructor
public class AuthController {
	private final AuthService authService;

	/**
	 * @see nbc.newsfeed.common.util.AuthCookieUtil addAuthCookies
	 * @param request email, password
	 * @return response accessToken, accessTokenIssuedAt, accessTokenExpiredAt, refreshToken refreshTokenIssuedAt, refreshTokenExpiredAt
	 */
	@PostMapping("/api/auth/token")
	public ResponseEntity<TokenResponse> signin(
		@Valid @RequestBody SigninRequest request,
		HttpServletResponse response) {
		Token token = authService.generateToken(request.email(), request.password());
		addAuthCookies(response, token);
		return ResponseEntity.ok(TokenResponse.from(token));
	}
}
