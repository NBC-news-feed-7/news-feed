package nbc.newsfeed.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nbc.newsfeed.common.error.CustomException;
import nbc.newsfeed.common.error.ErrorCode;

@AllArgsConstructor
@Getter
public enum FriendRequestStatus {

    REQUESTED(0, "요청 중"),
    READ(1, "요청 확인됨"),
    ACCEPTED(2, "요청 수락됨"),
    REJECTED(3, "요청 거절됨");

    private final int code;
    private final String description;

    public static FriendRequestStatus fromCode(int code) {
        for (FriendRequestStatus status : values()) {
            if (status.getCode() == code) {
                return status;
            }
        }
        throw new CustomException(ErrorCode.INVALID_STATUS);
    }
}
