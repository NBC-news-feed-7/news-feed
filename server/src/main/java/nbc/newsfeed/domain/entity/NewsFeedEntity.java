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
@Table(name = "news_feeds")
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

	public void validateCanLike(UserEntity user, NewsFeedLikeRepository likeRepository) {
		if (likeRepository.existsByNewsFeedAndUser(this, user)) {
			throw new CustomException(ErrorCode.ALREADY_LIKED);
		}
	}

	public void validateCanCancelLike(UserEntity user, NewsFeedLikeRepository likeRepository) {
		if (!likeRepository.existsByNewsFeedAndUser(this, user)) {
			throw new CustomException(ErrorCode.NOT_LIKED_YET);
		}
	}


	public void visibleNewsFeed(NewsFeedUseYn useYn) {
		this.useYn = useYn;
	}

	public void update(NewsFeedRequestDto requestDto) {
		this.title = requestDto.getTitle();
		this.content = requestDto.getContent();
	}
}
