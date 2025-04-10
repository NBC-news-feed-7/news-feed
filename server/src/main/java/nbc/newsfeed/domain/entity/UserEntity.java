package nbc.newsfeed.domain.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
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
@Table(name = "users")
@SQLDelete(sql = "UPDATE users SET deleted_at = NOW() WHERE id = ?") // soft delete
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
	private LocalDateTime deletedAt; // deletedAt이 Null이 아니면 삭제 되었다고 판단

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

	// 기본 프로필이미지 Setting
	// TODO 경로 프로퍼티로 관리 하는게 좋음
	@PrePersist
	public void prePersist() {
		if (this.profileImageUrl == null) {
			this.profileImageUrl = "uploads/user/profile-images/default-profile.jpg";
		}
	}
}