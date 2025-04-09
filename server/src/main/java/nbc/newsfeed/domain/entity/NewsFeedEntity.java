package nbc.newsfeed.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import nbc.newsfeed.domain.dto.newsfeed.NewsFeedRequestDto;
import java.time.LocalDateTime;
import org.hibernate.annotations.SQLRestriction;


@ToString
@Getter
@Builder
@SQLRestriction("deleted_at IS NULL")
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
}
