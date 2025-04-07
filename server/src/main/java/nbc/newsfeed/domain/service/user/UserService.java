package nbc.newsfeed.domain.service.user;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import nbc.newsfeed.common.error.CustomException;
import nbc.newsfeed.common.error.ErrorCode;
import nbc.newsfeed.domain.entity.UserEntity;
import nbc.newsfeed.domain.repository.UserRepository;

/**
 * @author    : kimjungmin
 * Created on : 2025. 4. 7.
 */
@Service
@RequiredArgsConstructor
public class UserService {
	private final PasswordEncoder passwordEncoder;
	private final UserRepository userRepository;

	@Transactional
	public UserEntity register(final String email, final String password, final String nickname) {
		if (userRepository.existsByEmail(email)) {
			throw new CustomException(ErrorCode.DUPLICATED_EMAIL);
		}

		final String encryptedPassword = passwordEncoder.encode(password);
		UserEntity user = UserEntity.withRegisterInfo(email, encryptedPassword, nickname);

		return userRepository.save(user);
	}
}
