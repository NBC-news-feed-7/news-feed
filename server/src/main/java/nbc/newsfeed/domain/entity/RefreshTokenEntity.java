package nbc.newsfeed.domain.entity;

import java.time.LocalDateTime;
import java.time.ZoneId;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import nbc.newsfeed.domain.service.auth.model.Token;

@ToString
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "refresh_tokens")
@Entity
public class RefreshTokenEntity extends TimeBaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true, length = 512)
	private String accessToken;

	@Column(nullable = false)
	private LocalDateTime accessTokenExpiredAt;

	@Column(nullable = false, unique = true, length = 512)
	private String refreshToken;

	@Column(nullable = false)
	private LocalDateTime refreshTokenExpiredAt;

	public static RefreshTokenEntity of(String refreshToken, LocalDateTime refreshTokenExpiredAt) {
		return RefreshTokenEntity.builder()
			.refreshToken(refreshToken)
			.refreshTokenExpiredAt(refreshTokenExpiredAt)
			.build();
	}

	public static RefreshTokenEntity of(Token token) {
		LocalDateTime accessTokenExpireDateTime = token.getAccessTokenExpiredAt().toInstant()
			.atZone(ZoneId.systemDefault())
			.toLocalDateTime();

		LocalDateTime refreshTokenExpireDateTime = token.getRefreshTokenExpiredAt().toInstant()
			.atZone(ZoneId.systemDefault())
			.toLocalDateTime();

		return RefreshTokenEntity.builder()
			.accessToken(token.getAccessToken())
			.accessTokenExpiredAt(accessTokenExpireDateTime)
			.refreshToken(token.getRefreshToken())
			.refreshTokenExpiredAt(refreshTokenExpireDateTime)
			.build();
	}
}
