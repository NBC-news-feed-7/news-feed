package nbc.newsfeed.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;


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
