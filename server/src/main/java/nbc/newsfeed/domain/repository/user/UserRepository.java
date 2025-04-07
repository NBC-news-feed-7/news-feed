package nbc.newsfeed.domain.repository.user;

import nbc.newsfeed.domain.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
}
