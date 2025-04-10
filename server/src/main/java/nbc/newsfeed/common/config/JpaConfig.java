package nbc.newsfeed.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * @author    : kimjungmin
 * Created on : 2025. 4. 7.
 */
@EnableJpaAuditing // CreatedAt, UpdatedAt 기능 추가
@Configuration
public class JpaConfig {
}
