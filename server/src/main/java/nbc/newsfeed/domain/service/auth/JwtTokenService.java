package nbc.newsfeed.domain.service.auth;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import nbc.newsfeed.common.config.JwtConfig;
import nbc.newsfeed.domain.service.auth.model.Token;
import nbc.newsfeed.domain.service.auth.model.TokenClaim;

/**
 * @author : kimjungmin
 * Created on : 2025. 3. 23.
 */
@Service
@RequiredArgsConstructor
public class JwtTokenService implements TokenService {

	private final JwtConfig jwtConfig;

	@Override
	public Token generateToken(TokenClaim tokenClaim) {
		final long now = System.currentTimeMillis();
		Date issuedDate = new Date(now);
		Date accessTokenexpireDate = new Date(now + jwtConfig.getAccessToken().expire());
		Date refreshTokenexpireDate = new Date(now + jwtConfig.getRefreshToken().expire());

		SecretKey accessTokenSecretKey = Keys.hmacShaKeyFor(jwtConfig.getAccessToken().secret().getBytes());
		SecretKey refreshTokenSecretKey = Keys.hmacShaKeyFor(jwtConfig.getRefreshToken().secret().getBytes());

		final String accessToken = Jwts.builder()
			.subject(tokenClaim.getSubject().toString())
			.claim("email", tokenClaim.getEmail())
			.claim("nickname", tokenClaim.getNickname())
			.claim("roles", tokenClaim.getRoles())
			.claim("jti", UUID.randomUUID().toString())
			.issuedAt(issuedDate)
			.expiration(accessTokenexpireDate)
			.signWith(accessTokenSecretKey)
			.compact();

		final String refreshToken = Jwts.builder()
			.subject(tokenClaim.getSubject().toString())
			.claim("email", tokenClaim.getEmail())
			.claim("nickname", tokenClaim.getNickname())
			.claim("roles", tokenClaim.getRoles())
			.claim("jti", UUID.randomUUID().toString())
			.issuedAt(issuedDate)
			.expiration(refreshTokenexpireDate)
			.signWith(refreshTokenSecretKey)
			.compact();

		return new Token(
			accessToken,
			issuedDate,
			accessTokenexpireDate,
			refreshToken,
			issuedDate,
			refreshTokenexpireDate
		);
	}

	@Override
	public TokenClaim parseToken(final String token) {
		SecretKey secretKey = Keys.hmacShaKeyFor(jwtConfig.getRefreshToken().secret().getBytes());
		Jws<Claims> claimsJws = Jwts.parser().verifyWith(secretKey).build()
			.parseSignedClaims(token);

		final Long userId = Long.valueOf(claimsJws.getBody().getSubject());
		final String email = claimsJws.getBody().get("email", String.class);
		final String nickname = claimsJws.getBody().get("nickname", String.class);
		List<?> roles = claimsJws.getPayload().get("roles", List.class);

		return new TokenClaim(userId, email, nickname, roles.stream().map(Object::toString).toList());
	}
}
