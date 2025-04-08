package nbc.newsfeed.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import nbc.newsfeed.domain.repository.commentLike.CommentLikeRepository;

import java.util.Optional;


@ToString
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "comments")
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

	private Integer useYn;


	public void toggleLike(UserEntity user, CommentLikeRepository likeRepository) {
		Optional<CommentLikeEntity> existing = likeRepository.findByCommentAndUser(this, user);
		existing.ifPresentOrElse(
				likeRepository::delete,
				() -> likeRepository.save(CommentLikeEntity.of(this, user))
		);
	}

}
