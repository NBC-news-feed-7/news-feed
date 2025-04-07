package nbc.newsfeed.domain.controller.user;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nbc.newsfeed.domain.dto.user.request.CreateUserRequest;
import nbc.newsfeed.domain.dto.user.request.UpdateUserRequest;
import nbc.newsfeed.domain.dto.user.response.UserResponse;
import nbc.newsfeed.domain.entity.UserEntity;
import nbc.newsfeed.domain.service.user.UserService;

/**
 * @author    : kimjungmin
 * Created on : 2025. 4. 7.
 */
@RestController
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

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

	@PutMapping("/api/users/{userId}")
	public ResponseEntity<UserResponse> updateUser(@Valid @RequestBody UpdateUserRequest request,
		@PathVariable Long userId) {
		UserEntity user = userService.update(userId, request.password(), request.updatedPassword(),
			request.nickname());
		return ResponseEntity.ok(UserResponse.fromEntity(user));
	}
}
