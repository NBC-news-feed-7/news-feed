package nbc.newsfeed.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;

@ToString
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED) // JPA 때문에 무조건 있어야함
@Table(name = "users")
@SQLDelete(sql = "UPDATE users SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at is NULL") // where 는 depreacted 되었다고한다!
@Entity
public class UserEntity extends TimeBaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 10)
	private String nickname;

	@Column(nullable = false, unique = true, length = 255)
	private String email;

	@Column(nullable = false, length = 255)
	private String password;

	@Column
	private String profileImageUrl;

	@Column(nullable = true)
	private LocalDateTime deletedAt;

	public static UserEntity withRegisterInfo(String email, String password, String nickname) {
		return UserEntity.builder()
			.email(email)
			.password(password)
			.nickname(nickname)
			.build();
	}

	public void update(String password, String nickname) {
		this.password = password;
		this.nickname = nickname;
	}

	public void changeProfileImage(String profileImageUrl) {
		this.profileImageUrl = profileImageUrl;
	}
}