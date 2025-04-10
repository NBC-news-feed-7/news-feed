package nbc.newsfeed.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@ConfigurationProperties(prefix = "jwt") // 객체에 바인딩 해서 사용하기 위함
public class JwtConfig {

	private final AccessToken accessToken;
	private final RefreshToken refreshToken;

	public record AccessToken(String secret, long expire) {
	}

	public record RefreshToken(String secret, long expire) {
	}
}