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

// TODO Profile 이미지 외에 유저 파일을 관리할 일이 있나? 제거 예정
@ToString
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user_files")
@Entity
public class UserFileEntity extends TimeBaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String originalName;

	@Column(nullable = false, unique = true)
	private String path;

	private Long size;

	// TODO UUID 없이 path로 하면 굳이 저장 안해도 될거 같음
	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private UserEntity user;
}
