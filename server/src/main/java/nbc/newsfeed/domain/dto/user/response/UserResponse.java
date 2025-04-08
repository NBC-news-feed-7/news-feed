package nbc.newsfeed.domain.dto.user.response;

import java.time.LocalDateTime;

import nbc.newsfeed.domain.entity.UserEntity;

public record UserResponse(
	Long id,
	String email,
	String nickname,
	String profileImageUrl,
	LocalDateTime createdAt,
	LocalDateTime updatedAt
) {
	public static UserResponse fromEntity(UserEntity entity) {
		return new UserResponse(
			entity.getId(),
			entity.getEmail(),
			entity.getNickname(),
			entity.getProfileImageUrl(),
			entity.getCreatedAt(),
			entity.getUpdatedAt()
		);
	}
}

