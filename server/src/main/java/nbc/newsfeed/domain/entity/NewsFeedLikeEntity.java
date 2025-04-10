package nbc.newsfeed.domain.entity;

import jakarta.persistence.*;
import lombok.*;


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
