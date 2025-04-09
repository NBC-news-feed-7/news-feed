package nbc.newsfeed.domain.controller.user;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nbc.newsfeed.common.error.CustomException;
import nbc.newsfeed.common.error.ErrorCode;
import nbc.newsfeed.domain.dto.user.request.CreateUserRequest;
import nbc.newsfeed.domain.dto.user.request.DeleteUserRequest;
import nbc.newsfeed.domain.dto.user.request.UpdateUserRequest;
import nbc.newsfeed.domain.dto.user.response.UserResponse;
import nbc.newsfeed.domain.entity.UserEntity;
import nbc.newsfeed.domain.service.image.ImageService;
import nbc.newsfeed.domain.service.user.UserService;

/**
 * @author    : kimjungmin
 * Created on : 2025. 4. 7.
 */
@RestController
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;
	private final ImageService imageService;

	@GetMapping("/api/users/me")
	public ResponseEntity<UserResponse> getCurrentUser(
		Authentication authentication
	) {
		final Long userId = Long.parseLong(authentication.getName());
		UserEntity user = userService.findById(userId);
		return ResponseEntity.ok(UserResponse.fromEntity(user));
	}

	@PostMapping("/api/users")
	public ResponseEntity<UserResponse> createUser(@Valid @RequestBody CreateUserRequest request) {
		UserEntity user = userService.register(request.email(), request.password(), request.nickname());
		return ResponseEntity.ok(UserResponse.fromEntity(user));
	}

	@PostMapping("/api/users/profile-image")
	public ResponseEntity<UserResponse> uploadProfileImage(
		@RequestParam("profileImage") MultipartFile profileImage,
		Authentication authentication
	) {
		final Long userId = Long.parseLong(authentication.getName());
		if (profileImage.isEmpty()) {
			throw new CustomException(ErrorCode.EMPTY_PROFILE_IMAGE);
		}

		String profileImageUrl = imageService.uploadImage(profileImage);
		UserEntity user = userService.changeProfileImage(userId, profileImageUrl);
		return ResponseEntity.ok(UserResponse.fromEntity(user));
	}

	@PutMapping("/api/users/{userId}")
	public ResponseEntity<UserResponse> updateUser(@Valid @RequestBody UpdateUserRequest request,
		@PathVariable Long userId,
		Authentication authentication) {
		final Long currentUserId = Long.parseLong(authentication.getName());

		if (!userId.equals(currentUserId)) {
			throw new CustomException(ErrorCode.FORBIDDEN);
		}

		UserEntity user = userService.update(userId, request.password(), request.updatedPassword(),
			request.nickname());
		return ResponseEntity.ok(UserResponse.fromEntity(user));
	}

	@DeleteMapping("/api/users/{userId}")
	public ResponseEntity<Void> deleteUser(@PathVariable Long userId,
		@Valid @RequestBody DeleteUserRequest request,
		Authentication authentication) {
		final Long currentUserId = Long.parseLong(authentication.getName());

		if (!userId.equals(currentUserId)) {
			throw new CustomException(ErrorCode.FORBIDDEN);
		}

		userService.deleteById(userId, request.password());

		return ResponseEntity.ok().build();
	}
}
