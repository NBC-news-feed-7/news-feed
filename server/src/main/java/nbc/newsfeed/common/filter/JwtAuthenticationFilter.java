package nbc.newsfeed.common.filter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.ExpiredJwtException;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nbc.newsfeed.common.error.ErrorCode;
import nbc.newsfeed.domain.service.auth.TokenService;
import nbc.newsfeed.domain.service.auth.model.TokenClaim;

/**
 * @author    : kimjungmin
 * Created on : 2025. 3. 24.
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private static final String AUTHENTICATION_HEADER = "Authorization";
	private static final String JWT_TOKEN_PREFIX = "Bearer ";
	private final TokenService tokenService;
	private final ObjectMapper objectMapper;

	public JwtAuthenticationFilter(TokenService tokenService, ObjectMapper objectMapper) {
		this.tokenService = tokenService;
		this.objectMapper = objectMapper;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
		throws ServletException, IOException {
		String authorizationHeader = request.getHeader(AUTHENTICATION_HEADER);
		if (StringUtils.isNotBlank(authorizationHeader) && authorizationHeader.startsWith(JWT_TOKEN_PREFIX)) {
			String accessToken = authorizationHeader.substring(7);
			try {
				TokenClaim tokenClaim = tokenService.parseToken(accessToken);
				List<SimpleGrantedAuthority> authorities = tokenClaim.getRoles()
					.stream()
					.map(SimpleGrantedAuthority::new)
					.toList();
				// TODO 인증에 유저 아이디 들어 있음 공유 필요
				Authentication authentication = new TestingAuthenticationToken(
					tokenClaim.getSubject(), "password", authorities
				);
				SecurityContextHolder.getContext().setAuthentication(authentication);
			} catch (ExpiredJwtException e) {
				// 토큰 만료되었을 경우
				response.setStatus(HttpStatus.UNAUTHORIZED.value());
				response.setCharacterEncoding(StandardCharsets.UTF_8.name());
				response.setContentType(MediaType.APPLICATION_JSON_VALUE);

				response.getWriter()
					.write(objectMapper.writeValueAsString(
						ResponseEntity.status(HttpStatus.UNAUTHORIZED)
							.body(Map.of("message", ErrorCode.AUTH_TOKEN_EXPIRED.getMessage())))
					);
				return;
			}
		}

		filterChain.doFilter(request, response);
	}
}
