package nbc.newsfeed.domain.service.auth;

import nbc.newsfeed.domain.service.auth.model.Token;
import nbc.newsfeed.domain.service.auth.model.TokenClaim;

/**
 * @author    : kimjungmin
 * Created on : 2025. 3. 23.
 */
public interface TokenService {
	Token generateToken(TokenClaim tokenClaim);

	TokenClaim parseToken(String token);
}
