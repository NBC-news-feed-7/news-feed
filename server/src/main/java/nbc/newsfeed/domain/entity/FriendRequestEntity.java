package nbc.newsfeed.domain.entity;

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

@ToString
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED) // JPA 때문에 무조건 있어야함
@Table(name = "friend_requests")
@Entity
public class FriendRequestEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "from_user_id", nullable = false)
	private UserEntity fromUser;

	@ManyToOne
	@JoinColumn(name = "to_user_id", nullable = false)
	private UserEntity toUser;

	// TODO 상태 추가 예정
}
