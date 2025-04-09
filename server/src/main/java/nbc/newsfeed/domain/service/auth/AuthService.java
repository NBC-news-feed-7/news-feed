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

	@Transactional
	public Token generateToken(final String email, final String password) {
		UserEntity user = userRepository.findByEmail(email)
			.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

		if (!passwordEncoder.matches(password, user.getPassword())) {
			throw new CustomException(ErrorCode.PASSWORD_MISMATCH);
		}

		TokenClaim tokenClaim = new TokenClaim(user.getId(), user.getEmail(), user.getNickname(), List.of());
		Token token = tokenService.generateToken(tokenClaim);

		//  아 이게 드물게 겹치는 토큰이 생기긴하네 ;
		refreshTokenRepository.save(RefreshTokenEntity.of(token));

		return token;
	}
}
