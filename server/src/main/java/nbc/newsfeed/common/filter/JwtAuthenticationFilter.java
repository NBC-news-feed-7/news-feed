package nbc.newsfeed.common.filter;

import static nbc.newsfeed.common.util.AuthCookieUtil.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nbc.newsfeed.common.error.CustomException;
import nbc.newsfeed.common.error.ErrorCode;
import nbc.newsfeed.common.error.ErrorResponseDto;
import nbc.newsfeed.domain.entity.RefreshTokenEntity;
import nbc.newsfeed.domain.repository.refreshtoken.RefreshTokenRepository;
import nbc.newsfeed.domain.service.auth.TokenService;
import nbc.newsfeed.domain.service.auth.model.Token;
import nbc.newsfeed.domain.service.auth.model.TokenClaim;

/**
 * @author : kimjungmin
 * Created on : 2025. 4. 9.
 */
@Slf4j
@Getter
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	private final TokenService tokenService;
	private final ObjectMapper objectMapper;
	private final RefreshTokenRepository refreshTokenRepository;

	// ParseToken JwtAuthenticationFilter 내부 클래스
	// 요청에서 파싱된 AccessToken, RefreshToken을 표현하기 위한 클래스
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
		throws ServletException, IOException {
		ParsedToken parsedToken = new ParsedToken(request.getCookies());
		// 토큰이 둘 다 있어야 인증된 상태로 인정
		if (parsedToken.isAccessTokenAndRefreshTokenExist()) {
			try {
				// TokenClaim(subejct:userId, email, nickname, roles)
				TokenClaim tokenClaim = tokenService.parseToken(parsedToken.getAccessToken());
				makeSecurityAuthentication(tokenClaim);
			} catch (ExpiredJwtException e) {
				// 쿠키를 보낼 때는 만료가 아니었으나 도착 했을 때 만료되었을 수 있다.
				try {
					refreshToken(response, parsedToken);
				} catch (Exception ex) {
					throw ex;
				}
			} catch (Exception e) {
				processInvalidToken(request, response);
				return;
			}
		}

		// 액세스 토큰이 만료되면 요청에 쿠키가 없다.
		if (parsedToken.isAccessTokenNotExistsAndRefreshTokenExists()) {
			try {
				refreshToken(response, parsedToken);
			} catch (Exception e) {
				processInvalidToken(request, response);
				return;
			}
		}

		filterChain.doFilter(request, response);
	}

	// 토큰 재발급
	private void refreshToken(
		HttpServletResponse response,
		ParsedToken parsedToken) {
		// refreshToken이 유효한 토큰인지 확인 하기 위해 DB에서 토큰 확인
		// 유효하지 않은 refreshToken은 DB에 존재하지 않음
		refreshTokenRepository.findByRefreshToken(parsedToken.getRefreshToken())
			.orElseThrow(() -> new CustomException(ErrorCode.INVALID_TOKEN));
		// refreshToken 파싱 후 인증 객체 생성
		TokenClaim tokenClaim = tokenService.parseToken(parsedToken.getRefreshToken());
		makeSecurityAuthentication(tokenClaim);
		// 새로운 토큰 기반으로 리프레시 토큰 재발급 DB에 남은 토큰은 스케줄러로 밀어버림
		Token newToken = tokenService.generateToken(tokenClaim);
		refreshTokenRepository.save(RefreshTokenEntity.of(newToken));
		// 쿠키에 토큰 세팅
		addAuthCookies(response, newToken);
	}

	// Token에 문제가 있을 경우 401예외 응답
	private void processInvalidToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		response.setCharacterEncoding(StandardCharsets.UTF_8.name());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);

		response.getWriter()
			.write(objectMapper.writeValueAsString(
				ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(ErrorResponseDto.from(ErrorCode.INVALID_TOKEN, request.getRequestURI())))
			);
	}

	// Token정보를 기반으로 Security Authentication Setting
	private void makeSecurityAuthentication(TokenClaim tokenClaim) {
		List<SimpleGrantedAuthority> authorities = tokenClaim.getRoles()
			.stream()
			.map(SimpleGrantedAuthority::new)
			.toList();

		Authentication authentication = new UsernamePasswordAuthenticationToken(
			tokenClaim.getSubject(), null, authorities
		);

		SecurityContextHolder.getContext().setAuthentication(authentication);
	}

	@Getter
	private static class ParsedToken {
		private final String accessToken;
		private final String refreshToken;

		public ParsedToken(Cookie[] cookies) {
			String accessToken = null;
			String refreshToken = null;

			if (cookies != null) {
				for (Cookie cookie : cookies) {
					if (cookie.getName().equals(ACCESS_TOKEN_COOKIE)) {
						accessToken = cookie.getValue();
						continue;
					}

					if (cookie.getName().equals(REFRESH_TOKEN_COOKIE)) {
						refreshToken = cookie.getValue();
					}
				}
			}

			this.accessToken = accessToken;
			this.refreshToken = refreshToken;
		}

		public boolean isAccessTokenAndRefreshTokenExist() {
			return accessToken != null && refreshToken != null;
		}

		public boolean isAccessTokenNotExistsAndRefreshTokenExists() {
			return accessToken == null && refreshToken != null;
		}
	}
}


