package nbc.newsfeed.domain.service.auth;

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

	// Token 생성
	@Transactional
	public Token generateToken(final String email, final String password) {
		// 존재하는 유저인지 확인
		UserEntity user = userRepository.findByEmail(email)
			.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
		// 비밀번호 일치 여부 확인
		if (!passwordEncoder.matches(password, user.getPassword())) {
			throw new CustomException(ErrorCode.PASSWORD_MISMATCH);
		}
		// 토큰 정보 생성 ID, Email, nickname, Roles -> 현재는 권한 관리 X
		TokenClaim tokenClaim = new TokenClaim(user.getId(), user.getEmail(), user.getNickname(), List.of());
		// Token(accessToken, refreshToken)
		Token token = tokenService.generateToken(tokenClaim);
		// refreshToken 저장
		refreshTokenRepository.save(RefreshTokenEntity.of(token));

		return token;
	}
}
