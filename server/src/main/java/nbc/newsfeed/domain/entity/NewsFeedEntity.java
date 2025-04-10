package nbc.newsfeed.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import nbc.newsfeed.common.error.CustomException;
import nbc.newsfeed.common.error.ErrorCode;
import nbc.newsfeed.domain.dto.newsfeed.NewsFeedRequestDto;
import nbc.newsfeed.domain.dto.newsfeed.NewsFeedUseYn;
import static nbc.newsfeed.domain.dto.newsfeed.NewsFeedUseYn.Y;
import nbc.newsfeed.domain.repository.newsfeedLike.NewsFeedLikeRepository;

import java.util.Optional;



@ToString
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "news_feeds", indexes = {
		@Index(name = "idx_news_title", columnList = "title"),
		@Index(name = "idx_news_content", columnList = "content"),
		@Index(name = "idx_news_updated_at", columnList = "updated_at")
})
@Entity
public class NewsFeedEntity extends TimeBaseEntity{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String title;

	@Column(nullable = false)
	private String content;

	// 사용여부 ENUM?!
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	@Builder.Default
	private NewsFeedUseYn useYn = Y;

	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private UserEntity user;

	public void visibleNewsFeed(NewsFeedUseYn useYn) {
		this.useYn = useYn;
	}

	public void update(NewsFeedRequestDto requestDto) {
		this.title = requestDto.getTitle();
		this.content = requestDto.getContent();
	}
}
