package nbc.newsfeed.domain.repository.refreshtoken;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import nbc.newsfeed.domain.entity.RefreshTokenEntity;

/**
 * @author    : kimjungmin
 * Created on : 2025. 4. 7.
 */
public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Long> {
	Optional<RefreshTokenEntity> findByRefreshToken(String refreshToken);

	@Transactional
	@Modifying
	@Query("delete from RefreshTokenEntity r where r.refreshTokenExpiredAt < :now")
	void deleteAllExpiredSince(LocalDateTime now);
}
