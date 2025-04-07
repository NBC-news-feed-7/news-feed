package nbc.newsfeed.domain.repository.refreshtoken;

import org.springframework.data.jpa.repository.JpaRepository;

import nbc.newsfeed.domain.entity.RefreshTokenEntity;

/**
 * @author    : kimjungmin
 * Created on : 2025. 4. 7.
 */
public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Long> {
}
