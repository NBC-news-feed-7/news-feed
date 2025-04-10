package nbc.newsfeed.domain.service.user;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import nbc.newsfeed.common.error.CustomException;
import nbc.newsfeed.common.error.ErrorCode;
import nbc.newsfeed.domain.entity.UserEntity;
import nbc.newsfeed.domain.repository.user.UserRepository;

/**
 * @author    : kimjungmin
 * Created on : 2025. 4. 7.
 */
@Service
@RequiredArgsConstructor
public class UserService {
	private final PasswordEncoder passwordEncoder;
	private final UserRepository userRepository;

	/**
	 * @param email
	 * @param password
	 * @param nickname
	 * @return UserEntity(id, email, nickname, profileImageUrl, createdAt, updatedAt, deletedAt)
	 */
	@Transactional
	public UserEntity register(final String email, final String password, final String nickname) {
		if (userRepository.existsByEmail(email)) {
			throw new CustomException(ErrorCode.DUPLICATED_EMAIL);
		}
		// 비밀번호 암호화
		final String encryptedPassword = passwordEncoder.encode(password);
		UserEntity user = UserEntity.withRegisterInfo(email, encryptedPassword, nickname);

		return userRepository.save(user);
	}

	/**
	 * @param userId
	 * @param password 기존 비밀번호
	 * @param updatedPassword 새로운 비밀번호
	 * @param nickname
	 * @return UserEntity(id, email, nickname, profileImageUrl, createdAt, updatedAt, deletedAt)
	 */
	@Transactional
	public UserEntity update(
		final Long userId,
		final String password,
		final String updatedPassword,
		final String nickname) {
		UserEntity user = userRepository.findById(userId)
			.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

		if (!passwordEncoder.matches(password, user.getPassword())) {
			throw new CustomException(ErrorCode.PASSWORD_MISMATCH);
		}

		if (updatedPassword.equals(password)) {
			throw new CustomException(ErrorCode.SAME_PASSWORD);
		}

		String newEncryptedPassword = passwordEncoder.encode(updatedPassword);

		user.update(newEncryptedPassword, nickname);

		return user;
	}

	/**
	 *
	 * @param userId
	 * @param newProfileImageUrl
	 * @return UserEntity(id, email, nickname, newProfileImageUrl, createdAt, updatedAt, deletedAt)
	 */
	@Transactional
	public UserEntity changeProfileImage(final Long userId, final String newProfileImageUrl) {
		UserEntity user = userRepository.findById(userId)
			.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

		user.changeProfileImage(newProfileImageUrl);

		return user;
	}

	/**
	 *
	 * @param userId
	 * @return UserEntity(id, email, nickname, profileImageUrl, createdAt, updatedAt, deletedAt)
	 */
	@Transactional(readOnly = true)
	public UserEntity findById(final Long userId) {
		return userRepository.findById(userId)
			.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
	}

	/**
	 *
	 * @param userId
	 * @param password
	 * @return UserEntity(id, email, nickname, profileImageUrl, createdAt, updatedAt, deletedAt)
	 */
	@Transactional
	public void deleteById(final Long userId, final String password) {
		UserEntity user = userRepository.findById(userId)
			.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

		if (!passwordEncoder.matches(password, user.getPassword())) {
			throw new CustomException(ErrorCode.PASSWORD_MISMATCH);
		}

		userRepository.deleteById(userId);
	}
}
