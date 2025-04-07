package nbc.newsfeed.domain.repository.friendrequest;

import nbc.newsfeed.domain.entity.FriendRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FriendRequestRepository extends JpaRepository<FriendRequestEntity, Long> {

    // 친구 요청 중복 여부 확인용
    boolean existsByFromUserIdAndToUserId(Long fromUserId, Long toUserId);
}
