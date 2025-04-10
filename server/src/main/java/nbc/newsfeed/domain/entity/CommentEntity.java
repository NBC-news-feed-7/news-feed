package nbc.newsfeed.domain.entity;

import jakarta.persistence.*;
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
		name = "comments",
		indexes = {
				@Index(name = "idx_news_feed_id", columnList = "news_feed_id"),
				@Index(name = "idx_user_id", columnList = "user_id"),
				@Index(name = "idx_created_at", columnList = "created_at")
		}
)

@Entity
public class CommentEntity extends TimeBaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String content;

	@ManyToOne
	@JoinColumn(name = "parent_comment_id", nullable = true)
	private CommentEntity parentComment;

	@ManyToOne
	@JoinColumn(name = "news_feed_id", nullable = false)
	private NewsFeedEntity newsFeed;

	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private UserEntity user;
	@Column(nullable = false, columnDefinition = "INT DEFAULT 1")
	@Builder.Default
	private Integer useYn = 1;


	public void updateContent(String content) {
		this.content = content;
	}
	public void delete() {
		this.useYn = 0;
	}


}
