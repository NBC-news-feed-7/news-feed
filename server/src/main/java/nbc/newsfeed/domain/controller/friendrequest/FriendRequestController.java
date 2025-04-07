package nbc.newsfeed.domain.controller.friendrequest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nbc.newsfeed.domain.entity.FriendRequestRequestDto;
import nbc.newsfeed.domain.entity.FriendRequestResponseDto;
import nbc.newsfeed.domain.service.friendrequest.FriendRequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/friend-requests")
public class FriendRequestController {

    private final FriendRequestService friendRequestService;

    @PostMapping
    public ResponseEntity<FriendRequestResponseDto> requestFriend(@RequestBody @Valid FriendRequestRequestDto dto) {
        Long fromUserId = 1L; // 임시 인증된 유저 ID
        return ResponseEntity.ok(friendRequestService.createRequest(fromUserId, dto));
    }
}
