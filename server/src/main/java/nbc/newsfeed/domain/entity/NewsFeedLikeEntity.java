package nbc.newsfeed.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import nbc.newsfeed.domain.repository.newsfeedLike.NewsFeedLikeRepository;

import java.util.Optional;


@ToString
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
		name = "news_feed_likes",
		uniqueConstraints = {
				@UniqueConstraint(columnNames = {"news_feeds_id", "user_id"})
		}
)
@Entity
public class NewsFeedLikeEntity extends TimeBaseEntity{
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
