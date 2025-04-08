package nbc.newsfeed.domain.controller.friendrequest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nbc.newsfeed.domain.dto.friendrequest.request.FriendRequestRequestDto;
import nbc.newsfeed.domain.dto.friendrequest.response.FriendRequestResponseDto;
import nbc.newsfeed.domain.service.friendrequest.FriendRequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.Authentication;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/friend-requests")
public class FriendRequestController {

    private final FriendRequestService friendRequestService;

    @PostMapping("/api/friend-requests")
    public ResponseEntity<FriendRequestResponseDto> requestFriend(
            Authentication authentication,
            @Valid @RequestBody FriendRequestRequestDto dto
    ) {
        final Long fromUserId = Long.parseLong(authentication.getName());
        return ResponseEntity.ok(friendRequestService.createRequest(fromUserId, dto));
    }
}
