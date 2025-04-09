package nbc.newsfeed.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@ToString
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "comments_likes", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"comment_id", "user_id"})
    }, indexes = {
        @Index(name = "idx_like_comment_user", columnList = "user_id")
    }
)
@Entity
public class CommentLikeEntity extends TimeBaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id", nullable = false)
    private CommentEntity comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;


    public static CommentLikeEntity of(CommentEntity comment, UserEntity user) {
        return CommentLikeEntity.builder()
                .comment(comment)
                .user(user)
                .build();
    }

}
