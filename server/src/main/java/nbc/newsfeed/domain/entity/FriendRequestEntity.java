package nbc.newsfeed.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import nbc.newsfeed.domain.dto.friendrequest.FriendRequestStatus;

@ToString
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
		name = "friend_requests",
		indexes = {
				@Index(name = "idx_to_user_id", columnList = "to_user_id"),
				@Index(name = "idx_from_to", columnList = "from_user_id, to_user_id"),
				@Index(name = "idx_status", columnList = "status")
		}
)
@Entity
public class FriendRequestEntity extends TimeBaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "from_user_id", nullable = false)
	private UserEntity fromUser;

	@ManyToOne
	@JoinColumn(name = "to_user_id", nullable = false)
	private UserEntity toUser;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private FriendRequestStatus status;

	public void updateStatus(FriendRequestStatus status) {
		this.status = status;
	}
}
