package nbc.newsfeed.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
	name = "news_feed_likes",
	uniqueConstraints = {
		@UniqueConstraint(columnNames = {"news_feeds_id", "user_id"})
	}, indexes = {
	@Index(name = "idx_like_user_feed", columnList = "user_id, news_feeds_id")
}
)
@Entity
public class NewsFeedLikeEntity extends TimeBaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "news_feeds_id", nullable = false)
	private NewsFeedEntity newsFeed;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private UserEntity user;

	public static NewsFeedLikeEntity of(NewsFeedEntity newsFeed, UserEntity user) {
		return NewsFeedLikeEntity.builder()
			.newsFeed(newsFeed)
			.user(user)
			.build();
	}

}
