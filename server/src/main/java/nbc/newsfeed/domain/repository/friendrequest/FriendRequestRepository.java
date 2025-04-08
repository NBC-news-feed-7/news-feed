package nbc.newsfeed.domain.repository.friendrequest;

import nbc.newsfeed.domain.entity.FriendRequestEntity;
import nbc.newsfeed.domain.entity.UserEntity;
import nbc.newsfeed.domain.dto.friendrequest.FriendRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendRequestRepository extends JpaRepository<FriendRequestEntity, Long> {

    // 친구 요청 중복 여부 확인용
    boolean existsByFromUserIdAndToUserId(Long fromUserId, Long toUserId);

    // 특정 유저에게 온 친구 요청 조회
    List<FriendRequestEntity> findAllByToUserId(Long toUserId);

    // 친구 요청 단건 조회 (요청자, 수신자 기준)
    Optional<FriendRequestEntity> findByFromUserIdAndToUserId(Long fromUserId, Long toUserId);

    // 상태에 따른 요청 목록 조회 (예: 요청한 친구 관계만 보기 등)
    List<FriendRequestEntity> findAllByFromUserAndStatus(UserEntity fromUser, FriendRequestStatus status);
    List<FriendRequestEntity> findAllByToUserAndStatus(UserEntity toUser, FriendRequestStatus status);

    boolean existsByFromUserIdAndToUserIdAndStatus(Long fromUserId, Long toUserId, FriendRequestStatus friendRequestStatus);
}
