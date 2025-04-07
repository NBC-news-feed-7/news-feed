package nbc.newsfeed.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import nbc.newsfeed.domain.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
	boolean existsByEmail(String email);
}
