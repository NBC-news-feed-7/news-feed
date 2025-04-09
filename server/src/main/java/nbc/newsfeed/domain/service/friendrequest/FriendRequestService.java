package nbc.newsfeed.domain.service.friendrequest;

import lombok.RequiredArgsConstructor;
import nbc.newsfeed.common.error.CustomException;
import nbc.newsfeed.common.error.ErrorCode;
import nbc.newsfeed.domain.dto.friendrequest.request.FriendRequestRequestDto;
import nbc.newsfeed.domain.dto.friendrequest.response.FriendRequestResponseDto;
import nbc.newsfeed.domain.dto.friendrequest.response.FriendRequestPageResponseDto;
import nbc.newsfeed.domain.entity.FriendRequestEntity;
import nbc.newsfeed.domain.dto.friendrequest.FriendRequestStatus;
import nbc.newsfeed.domain.entity.UserEntity;
import nbc.newsfeed.domain.repository.friendrequest.FriendRequestRepository;
import nbc.newsfeed.domain.repository.user.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FriendRequestService {

    private final FriendRequestRepository friendRequestRepository;
    private final UserRepository userRepository;

    /**
     * ✅ 친구 요청을 생성합니다.
     *
     * @param fromUserId 요청을 보낸 사용자 ID
     * @param dto 친구 요청 대상 정보
     * @return 생성된 친구 요청 정보
     */
    @Transactional
    public FriendRequestResponseDto createRequest(Long fromUserId, FriendRequestRequestDto dto) {
        Long toUserId = dto.getToUserId();

        if (fromUserId.equals(toUserId)) {
            throw new CustomException(ErrorCode.INVALID_STATUS);
        }

        if (friendRequestRepository.existsByFromUserIdAndToUserIdAndStatus(fromUserId, toUserId, FriendRequestStatus.ACCEPTED)) {
            throw new CustomException(ErrorCode.ALREADY_FRIEND);
        }

        boolean exists = friendRequestRepository.existsByFromUserIdAndToUserId(fromUserId, toUserId);
        if (exists) {
            throw new CustomException(ErrorCode.ALREADY_REQUESTED);
        }

        UserEntity fromUser = getUserOrThrow(fromUserId);
        UserEntity toUser = getUserOrThrow(toUserId);

        FriendRequestEntity request = FriendRequestEntity.builder()
                .fromUser(fromUser)
                .toUser(toUser)
                .status(FriendRequestStatus.REQUESTED)
                .build();

        FriendRequestEntity saved = friendRequestRepository.save(request);
        return FriendRequestResponseDto.from(saved);
    }

    /**
     * ✅ 특정 사용자에게 온 친구 요청들을 조회합니다.
     *
     * @param userId 사용자 ID
     * @return 친구 요청 리스트
     */
    @Transactional(readOnly = true)
    public List<FriendRequestResponseDto> getFriendRequests(Long userId) {
        return friendRequestRepository.findAllByToUserId(userId)
                .stream()
                .map(FriendRequestResponseDto::from)
                .toList();
    }

    /**
     * ✅ 친구 요청을 취소합니다.
     *
     * @param fromUserId 요청을 보낸 사용자 ID
     * @param toUserId 요청을 받은 사용자 ID
     */
    @Transactional
    public void cancelRequest(Long fromUserId, Long toUserId) {
        FriendRequestEntity request = friendRequestRepository.findByFromUserIdAndToUserId(fromUserId, toUserId)
                .orElseThrow(() -> new CustomException(ErrorCode.REQUEST_NOT_FOUND));

        if (request.getStatus() != FriendRequestStatus.REQUESTED) {
            throw new CustomException(ErrorCode.INVALID_CANCEL);
        }

        friendRequestRepository.delete(request);
    }

    /**
     * ✅ 친구 요청을 수락합니다.
     *
     * @param requestId 친구 요청 ID
     * @return 상태가 업데이트된 친구 요청 정보
     */
    @Transactional
    public FriendRequestResponseDto acceptRequest(Long requestId) {
        return updateStatus(requestId, FriendRequestStatus.ACCEPTED);
    }

    /**
     * ✅ 친구 요청을 거절합니다.
     *
     * @param requestId 친구 요청 ID
     * @return 상태가 업데이트된 친구 요청 정보
     */
    @Transactional
    public FriendRequestResponseDto rejectRequest(Long requestId) {
        return updateStatus(requestId, FriendRequestStatus.REJECTED);
    }

    /**
     * ✅ 친구 요청의 상태를 업데이트합니다.
     *
     * @param requestId 요청 ID
     * @param status 새로운 상태
     * @return 변경된 요청 정보
     */
    private FriendRequestResponseDto updateStatus(Long requestId, FriendRequestStatus status) {
        FriendRequestEntity request = friendRequestRepository.findById(requestId)
                .orElseThrow(() -> new CustomException(ErrorCode.REQUEST_NOT_FOUND));

        if (request.getStatus() != FriendRequestStatus.REQUESTED) {
            throw new CustomException(ErrorCode.INVALID_STATUS);
        }

        request.updateStatus(status);
        return FriendRequestResponseDto.from(request);
    }

    /**
     * ✅ 친구 관계를 삭제합니다.
     *
     * @param requestId 친구 요청 ID
     */
    @Transactional
    public void deleteFriend(Long requestId) {
        FriendRequestEntity request = friendRequestRepository.findById(requestId)
                .orElseThrow(() -> new CustomException(ErrorCode.REQUEST_NOT_FOUND));
        friendRequestRepository.delete(request);
    }

    /**
     * ✅ 사용자 ID로 사용자 정보를 조회합니다.
     *
     * @param userId 사용자 ID
     * @return 사용자 엔티티
     */
    private UserEntity getUserOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    /**
     * ✅ 모든 친구 요청을 페이징 처리하여 조회합니다.
     *
     * <p>요청 상태(`FriendRequestStatus`)를 기준으로 필터링할 수 있으며,
     * 상태 값이 없으면 전체 요청을 기준으로 조회합니다.</p>
     *
     * @param status   필터링할 요청 상태 (예: REQUESTED, ACCEPTED, REJECTED). null이면 전체 조회
     * @param pageable 페이징 및 정렬 정보 (예: page, size, sort 등)
     * @return 페이징된 친구 요청 DTO 목록
     */
    @Transactional(readOnly = true)
    public FriendRequestPageResponseDto<FriendRequestResponseDto> getAllFriendRequestsPaged(FriendRequestStatus status, Pageable pageable) {
        Page<FriendRequestEntity> page;

        if (status != null) {
            page = friendRequestRepository.findAllByStatus(status, pageable);
        } else {
            page = friendRequestRepository.findAll(pageable);
        }

        Page<FriendRequestResponseDto> dtoPage = page.map(FriendRequestResponseDto::from);
        return FriendRequestPageResponseDto.from(dtoPage);
    }
}
