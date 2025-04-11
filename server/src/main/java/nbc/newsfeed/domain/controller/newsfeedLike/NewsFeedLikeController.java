package nbc.newsfeed.domain.controller.newsfeedLike;

import lombok.RequiredArgsConstructor;
import nbc.newsfeed.domain.dto.newsfeed.NewsFeedPageResponseDto;
import nbc.newsfeed.domain.dto.newsfeedLike.LikeUserResponseDto;
import nbc.newsfeed.domain.service.newsfeedLike.NewsFeedLikeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/feeds")
public class NewsFeedLikeController {
    private final NewsFeedLikeService newsFeedLikeService;

    /**
     * 피드 좋아요 추가
     *
     * @param newsId 피드 ID
     * @param authentication 토큰 회원 정보
     * @return 201 created 반환
     */
    @PostMapping("/{newsId}/likes")
    public ResponseEntity<Void> addLike(
            @PathVariable Long newsId,
            Authentication authentication
    ) {
        Long userId = Long.parseLong(authentication.getName());
        newsFeedLikeService.addLike(newsId, userId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 피드 좋아요 삭제
     *
     * @param newsId 피드 ID
     * @param authentication 토큰 회원 정보
     * @return 204 nocontent 반환
     */
    @DeleteMapping("/{newsId}/likes")
    public ResponseEntity<Void> removeLike(
            @PathVariable Long newsId,
            Authentication authentication
    ) {
        Long userId = Long.parseLong(authentication.getName());
        newsFeedLikeService.removeLike(newsId, userId);
        return ResponseEntity.noContent().build();
    }

    /**
     * 피드 좋아요 여부 조회
     *
     * @param newsId 피드 ID
     * @param authentication 토큰 회원 정보
     * @return 200 OK 반환
     */
    @GetMapping("/{newsId}/likes/me")
    public ResponseEntity<Map<String, Boolean>> hasLiked(
            @PathVariable Long newsId,
            Authentication authentication
    ) {
        Long userId = Long.parseLong(authentication.getName());
        boolean liked = newsFeedLikeService.hasUserLikedFeed(newsId, userId);
        return ResponseEntity.ok(Collections.singletonMap("liked", liked));
    }

    /**
     * 피드 좋아요누른 사람 목록
     *
     * @param newsId 피드 ID
     * @return 200 OK 출력, LikeUserResponseDto
     */
    @GetMapping("/{newsId}/like-users")
    public ResponseEntity<List<LikeUserResponseDto>> getLikeUsers(
            @PathVariable Long newsId
    ) {
        List<LikeUserResponseDto> likeUsers = newsFeedLikeService.getLikeUsers(newsId);
        return new ResponseEntity<>(likeUsers, HttpStatus.OK);
    }

    /**
     * 내 좋아요 누른 피드 목록 확인
     *
     * @param pageable
     * @return 200 OK 출력, NewsFeedPageResponseDto
     */
    @GetMapping("/users/me/likes")
    public ResponseEntity<Page<NewsFeedPageResponseDto>> getLikedFeeds(
            Authentication authentication,
            Pageable pageable
    ) {
        Long userId = Long.parseLong(authentication.getName());
        Page<NewsFeedPageResponseDto> result = newsFeedLikeService.getLikedFeeds(userId, pageable);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
