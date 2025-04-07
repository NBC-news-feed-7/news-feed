package nbc.newsfeed.domain.service.auth;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import nbc.newsfeed.common.error.CustomException;
import nbc.newsfeed.common.error.ErrorCode;
import nbc.newsfeed.domain.entity.RefreshTokenEntity;
import nbc.newsfeed.domain.entity.UserEntity;
import nbc.newsfeed.domain.repository.refreshtoken.RefreshTokenRepository;
import nbc.newsfeed.domain.repository.user.UserRepository;
import nbc.newsfeed.domain.service.auth.model.Token;
import nbc.newsfeed.domain.service.auth.model.TokenClaim;

@Service
@RequiredArgsConstructor
public class AuthService {
	private final TokenService tokenService;
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final RefreshTokenRepository refreshTokenRepository;

	@Transactional
	public Token generateToken(final String email, final String password) {
		UserEntity user = userRepository.findByEmail(email)
			.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

		if (!passwordEncoder.matches(password, user.getPassword())) {
			throw new CustomException(ErrorCode.PASSWORD_MISMATCH);
		}

		TokenClaim tokenClaim = new TokenClaim(user.getId(), user.getEmail(), user.getNickname(), List.of());
		Token token = tokenService.generateToken(tokenClaim);
		LocalDateTime expiredDateTime = token.getRefreshTokenExpiredAt().toInstant()
			.atZone(ZoneId.systemDefault())
			.toLocalDateTime();

		RefreshTokenEntity refreshTokenEntity = RefreshTokenEntity.of(token.getRefreshToken(), expiredDateTime);
		refreshTokenRepository.save(refreshTokenEntity);

		return token;
	}

	@Transactional
	public Token refreshToken(final String refreshToken) {
		RefreshTokenEntity refreshTokenEntity = refreshTokenRepository.findByRefreshToken(refreshToken)
			.orElseThrow(() -> new CustomException(ErrorCode.AUTH_TOKEN_EXPIRED));

		TokenClaim tokenClaim = tokenService.parseToken(refreshTokenEntity.getRefreshToken());
		Token token = tokenService.generateToken(tokenClaim);
		LocalDateTime expiredDateTime = token.getRefreshTokenExpiredAt().toInstant()
			.atZone(ZoneId.systemDefault())
			.toLocalDateTime();

		refreshTokenRepository.deleteByRefreshToken(refreshToken);

		RefreshTokenEntity newRefreshTokenEntity = RefreshTokenEntity.of(token.getRefreshToken(), expiredDateTime);
		refreshTokenRepository.save(newRefreshTokenEntity);

		return token;
	}
}
