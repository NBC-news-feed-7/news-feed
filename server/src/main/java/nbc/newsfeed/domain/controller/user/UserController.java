package nbc.newsfeed.domain.controller.user;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nbc.newsfeed.domain.dto.user.request.CreateUserRequest;
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

	@PostMapping("/api/users")
	public ResponseEntity<UserResponse> createUser(@Valid @RequestBody CreateUserRequest request) {
		UserEntity user = userService.register(request.email(), request.password(), request.nickname());
		return ResponseEntity.ok(UserResponse.fromEntity(user));
	}
}
