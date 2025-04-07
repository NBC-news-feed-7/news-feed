package nbc.newsfeed.domain.service.auth.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author    : kimjungmin
 * Created on : 2025. 3. 23.
 */
@Getter
@AllArgsConstructor
public class TokenClaim {
	private String subject;
	private List<String> roles;
}
