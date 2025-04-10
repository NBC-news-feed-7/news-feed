package nbc.newsfeed.domain.scheduler;

import java.time.LocalDateTime;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nbc.newsfeed.domain.repository.refreshtoken.RefreshTokenRepository;

/**
 * @author    : kimjungmin
 * Created on : 2025. 4. 10.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RefreshTokenCleanupScheduler {
	private final RefreshTokenRepository repository;
	
	// @Scheduled(fixedRate = 10000) // 테스트용 10초마다 실행
	@Scheduled(cron = "0 0 3 * * *") // 새벽 3시
	public void cleanUpExpiredTokens() {
		LocalDateTime now = LocalDateTime.now();
		log.info("Clean up expired tokens since {}", now);
		repository.deleteAllExpiredSince(now);
		log.info("Clean up expired tokens done");
	}
}
