package nbc.newsfeed.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author    : kimjungmin
 * Created on : 2025. 4. 10.
 */
@Configuration
@EnableScheduling // MySQL에서 만료된 토큰을 지우기 위한 스케줄러 등록용
public class SchedulerConfig {
}
