package nbc.newsfeed.domain.controller.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nbc.newsfeed.domain.dto.auth.request.SigninRequest;
import nbc.newsfeed.domain.dto.auth.request.TokenRefreshRequest;
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

	@PostMapping("/api/auth/token")
	public ResponseEntity<TokenResponse> signin(@Valid @RequestBody SigninRequest request) {
		Token token = authService.generateToken(request.email(), request.password());
		return ResponseEntity.ok(TokenResponse.from(token));
	}

	@PostMapping("/api/auth/token/refresh")
	public ResponseEntity<TokenResponse> refreshToken(@Valid @RequestBody TokenRefreshRequest request) {
		Token token = authService.refreshToken(request.refreshToken());
		return ResponseEntity.ok(TokenResponse.from(token));
	}
}
