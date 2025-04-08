package nbc.newsfeed.domain.controller.friendrequest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nbc.newsfeed.domain.dto.friendrequest.FriendRequestStatus;
import nbc.newsfeed.domain.dto.friendrequest.request.FriendRequestRequestDto;
import nbc.newsfeed.domain.dto.friendrequest.response.FriendRequestResponseDto;
import nbc.newsfeed.domain.dto.friendrequest.response.FriendRequestPageResponseDto;
import nbc.newsfeed.domain.service.friendrequest.FriendRequestService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/friend-requests")
public class FriendRequestController {

    private final FriendRequestService friendRequestService;

    /**
     * ✅ 친구 요청을 생성합니다.
     *
     * @param authentication 현재 로그인된 사용자 정보
     * @param dto 친구 요청 대상 정보
     * @return 생성된 친구 요청 정보
     */
    @PostMapping
    public ResponseEntity<FriendRequestResponseDto> requestFriend(
            Authentication authentication,
            @Valid @RequestBody FriendRequestRequestDto dto
    ) {
        final Long fromUserId = Long.parseLong(authentication.getName());
        return ResponseEntity.ok(friendRequestService.createRequest(fromUserId, dto));
    }

    /**
     * ✅ 친구 요청 목록을 조회합니다.
     *
     * @return 친구 요청 목록
     */
    @GetMapping("/user")
    public ResponseEntity<List<FriendRequestResponseDto>> getFriendRequests(Authentication authentication) {
        Long currentUserId = Long.parseLong(authentication.getName());
        return ResponseEntity.ok(friendRequestService.getFriendRequests(currentUserId));
    }

    /**
     * ✅ 친구 요청을 취소합니다.
     *
     * @param authentication 현재 로그인된 사용자 정보
     * @param toUserId 요청을 받은 유저 ID
     * @return void
     */
    @DeleteMapping("/{toUserId}")
    public ResponseEntity<Void> cancelRequest(
            Authentication authentication,
            @PathVariable Long toUserId
    ) {
        final Long fromUserId = Long.parseLong(authentication.getName());
        friendRequestService.cancelRequest(fromUserId, toUserId);
        return ResponseEntity.noContent().build();
    }

    /**
     * ✅ 친구 요청을 수락합니다.
     *
     * @param friendRequestId 친구 요청 ID
     * @return 변경된 친구 요청 정보
     */
    @PutMapping("/{friendRequestId}/accept")
    public ResponseEntity<FriendRequestResponseDto> acceptRequest(@PathVariable Long friendRequestId) {
        return ResponseEntity.ok(friendRequestService.acceptRequest(friendRequestId));
    }

    /**
     * ✅ 친구 요청을 거절합니다.
     *
     * @param friendRequestId 친구 요청 ID
     * @return 변경된 친구 요청 정보
     */
    @PutMapping("/{friendRequestId}/reject")
    public ResponseEntity<FriendRequestResponseDto> rejectRequest(@PathVariable Long friendRequestId) {
        return ResponseEntity.ok(friendRequestService.rejectRequest(friendRequestId));
    }

    /**
     * ✅ 친구를 삭제합니다.
     *
     * @param friendRequestId 친구 요청 ID
     * @return void
     */
    @DeleteMapping("/{friendRequestId}/remove")
    public ResponseEntity<Void> deleteFriend(@PathVariable Long friendRequestId) {
        friendRequestService.deleteFriend(friendRequestId);
        return ResponseEntity.noContent().build();
    }

    /**
     * ✅ 모든 친구 요청을 페이징 처리하여 조회합니다.
     *
     * @param status   필터링할 상태 값 (선택적)
     * @param pageable 페이지, 크기, 정렬 기준 등
     * @return 페이징된 친구 요청 목록
     */
    @GetMapping("/all")
    public ResponseEntity<FriendRequestPageResponseDto<FriendRequestResponseDto>> getAllFriendRequests(
            @RequestParam(required = false) FriendRequestStatus status,
            Pageable pageable
    ) {
        return ResponseEntity.ok(friendRequestService.getAllFriendRequestsPaged(status, pageable));
    }
}
