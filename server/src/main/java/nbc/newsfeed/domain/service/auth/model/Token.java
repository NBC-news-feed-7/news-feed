package nbc.newsfeed.domain.service.auth.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author    : kimjungmin
 * Created on : 2025. 3. 23.
 */
@Getter
@AllArgsConstructor
public class Token {
	private String accessToken;
	private String refreshToken;
	private Date issuedAt;
	private Date expiredAt;
}
