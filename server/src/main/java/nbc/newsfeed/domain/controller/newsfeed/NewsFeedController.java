package nbc.newsfeed.domain.controller.newsfeed;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nbc.newsfeed.domain.dto.newsfeed.NewsFeedDetailResponseDto;
import nbc.newsfeed.domain.dto.newsfeed.NewsFeedDto;
import nbc.newsfeed.domain.dto.newsfeed.NewsFeedRequestDto;
import nbc.newsfeed.domain.service.newsfeed.NewsFeedService;
import nbc.newsfeed.domain.service.newsfeedLike.NewsFeedLikeService;

@Slf4j
@RestController
@RequestMapping("/api/feeds")
@RequiredArgsConstructor
public class NewsFeedController {

	private final NewsFeedService newsFeedService;
	private final NewsFeedLikeService newsFeedLikeService;

	@GetMapping("/{feedsId}")
	public ResponseEntity<NewsFeedDetailResponseDto> getNewsFeed(
		@PathVariable Long feedsId,
		Authentication authentication
	) {
		NewsFeedDto newsFeedDto = newsFeedService.getNewsFeed(feedsId);
		int likeCount = newsFeedLikeService.getLikeCount(feedsId);
		NewsFeedDetailResponseDto responseDto = NewsFeedDetailResponseDto.fromDto(newsFeedDto, likeCount);
		return ResponseEntity.ok(responseDto);
	}

	@PostMapping
	public ResponseEntity<NewsFeedDto> createNewsFeed(
		Authentication authentication,
		@Valid @RequestBody NewsFeedRequestDto requestDto
	) {
		Long userId = Long.parseLong(authentication.getName());
		NewsFeedDto responseDto = newsFeedService.createNewsFeed(userId, requestDto);
		return ResponseEntity.ok(responseDto);
	}

	@PutMapping("/{feedsId}")
	public ResponseEntity<NewsFeedDto> updateNewsFeed(
		@PathVariable Long feedsId,
		Authentication authentication,
		@Valid @RequestBody NewsFeedRequestDto requestDto
	) {
		Long userId = Long.parseLong(authentication.getName());
		NewsFeedDto responseDto = newsFeedService.updateNewsFeed(userId, feedsId, requestDto);
		return ResponseEntity.ok(responseDto);
	}

	@DeleteMapping("/{feedsId}")
	public ResponseEntity<Void> deleteNewsFeed(
		@PathVariable Long feedsId,
		Authentication authentication
	) {
		Long userId = Long.parseLong(authentication.getName());
		newsFeedService.deleteNewsFeed(userId, feedsId);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@GetMapping
	public ResponseEntity<Page<NewsFeedResponseDto>> getNewsFeedsBySort(
		@RequestParam(defaultValue = "LATEST") String sort,
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size
	) {
		nbc.newsfeed.domain.dto.newsfeeddto.NewsFeedSortType sortType = nbc.newsfeed.domain.dto.newsfeeddto.NewsFeedSortType.valueOf(
			sort.toUpperCase());
		Pageable pageable = PageRequest.of(page, size);
		Page<NewsFeedResponseDto> result = newsFeedService.getFeedsBySort(sortType, pageable);
		return ResponseEntity.ok(result);
	}
}
