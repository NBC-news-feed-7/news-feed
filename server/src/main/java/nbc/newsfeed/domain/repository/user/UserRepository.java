package nbc.newsfeed.domain.repository.user;

import java.util.Optional;

import nbc.newsfeed.common.error.CustomException;
import nbc.newsfeed.common.error.ErrorCode;
import org.springframework.data.jpa.repository.JpaRepository;

import nbc.newsfeed.domain.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
	boolean existsByEmail(String email);

	Optional<UserEntity> findByEmail(String email);

	default UserEntity getByIdOrThrow(Long userId) {
		return findById(userId)
				.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
	}

}
