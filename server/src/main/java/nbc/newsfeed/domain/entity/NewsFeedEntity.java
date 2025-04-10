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
import java.time.LocalDateTime;
import org.hibernate.annotations.SQLRestriction;


@ToString
@Getter
@Builder
@SQLRestriction("deleted_at IS NULL")
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


	@Column(nullable = true)
	private LocalDateTime deletedAt;


	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private UserEntity user;


	public void update(NewsFeedRequestDto requestDto) {
		this.title = requestDto.getTitle();
		this.content = requestDto.getContent();
	}

	public void sofeDelete(){
		this.deletedAt = LocalDateTime.now();
	}

	public void validateNotAuthor(UserEntity liker) {
		if (this.user.getId().equals(liker.getId())) {
			throw new CustomException(ErrorCode.CANNOT_LIKE_OWN_POST);
		}
	}

}
