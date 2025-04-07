package nbc.newsfeed.domain.service.friendrequest;

import lombok.RequiredArgsConstructor;
import nbc.newsfeed.common.error.CustomException;
import nbc.newsfeed.common.error.ErrorCode;
import nbc.newsfeed.domain.entity.FriendRequestRequestDto;
import nbc.newsfeed.domain.entity.FriendRequestResponseDto;
import nbc.newsfeed.domain.entity.FriendRequestEntity;
import nbc.newsfeed.domain.entity.FriendRequestStatus;
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

        UserEntity fromUser = userRepository.findById(fromUserId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        UserEntity toUser = userRepository.findById(toUserId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        FriendRequestEntity request = FriendRequestEntity.builder()
                .fromUser(fromUser)
                .toUser(toUser)
                .status(FriendRequestStatus.REQUESTED)
                .build();

        friendRequestRepository.save(request);

        return FriendRequestResponseDto.from(request);
    }
}
