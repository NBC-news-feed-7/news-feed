package nbc.newsfeed.domain.dto.friendrequest.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nbc.newsfeed.domain.dto.friendrequest.FriendRequestStatus;
import nbc.newsfeed.domain.entity.FriendRequestEntity;


@Getter
@AllArgsConstructor
public class FriendRequestResponseDto {
    private Long reqId;
    private Long fromUserId;
    private Long toUserId;
    private FriendRequestStatus status;

    public static FriendRequestResponseDto from(FriendRequestEntity entity) {
        return new FriendRequestResponseDto(
                entity.getId(),
                entity.getFromUser().getId(),
                entity.getToUser().getId(),
                entity.getStatus()
        );
    }
}
