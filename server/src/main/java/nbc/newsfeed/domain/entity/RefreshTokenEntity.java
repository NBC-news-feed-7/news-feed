package nbc.newsfeed.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@ToString
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "refresh_tokens")
@Entity
public class RefreshTokenEntity extends TimeBaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 512)
    private String refreshToken;

    @Column(nullable = false)
    private LocalDateTime expiredAt;

    public static RefreshTokenEntity of(String refreshToken, LocalDateTime expiredAt) {
        return RefreshTokenEntity.builder()
                .refreshToken(refreshToken)
                .expiredAt(expiredAt)
                .build();
    }
}
