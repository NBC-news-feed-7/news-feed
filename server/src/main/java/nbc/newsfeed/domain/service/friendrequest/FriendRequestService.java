package nbc.newsfeed.domain.service.friendrequest;

import lombok.RequiredArgsConstructor;
import nbc.newsfeed.common.error.CustomException;
import nbc.newsfeed.common.error.ErrorCode;
import nbc.newsfeed.domain.dto.friendrequest.request.FriendRequestRequestDto;
import nbc.newsfeed.domain.dto.friendrequest.response.FriendRequestResponseDto;
import nbc.newsfeed.domain.entity.FriendRequestEntity;
import nbc.newsfeed.domain.dto.friendrequest.FriendRequestStatus;
import nbc.newsfeed.domain.entity.UserEntity;
import nbc.newsfeed.domain.repository.friendrequest.FriendRequestRepository;
import nbc.newsfeed.domain.repository.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FriendRequestService {

    private final FriendRequestRepository friendRequestRepository;
    private final UserRepository userRepository;

    /**
     * ✅ 친구 요청 생성
     */
    @Transactional
    public FriendRequestResponseDto createRequest(Long fromUserId, FriendRequestRequestDto dto) {
        Long toUserId = dto.getToUserId();

        if (fromUserId.equals(toUserId)) {
            throw new CustomException(ErrorCode.INVALID_STATUS);
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
     * ✅ 유저 ID로 사용자 조회 (없으면 예외 발생)
     */
    private UserEntity getUserOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }
}
