package nbc.newsfeed.domain.repository.newsfeedLike;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import nbc.newsfeed.domain.dto.newsfeed.NewsFeedPageResponseDto;
import nbc.newsfeed.domain.dto.newsfeedLike.LikeUserResponseDto;
import nbc.newsfeed.domain.entity.QCommentEntity;
import nbc.newsfeed.domain.entity.QNewsFeedEntity;
import nbc.newsfeed.domain.entity.QNewsFeedLikeEntity;
import nbc.newsfeed.domain.entity.QNewsFileEntity;
import nbc.newsfeed.domain.entity.QUserEntity;

@RequiredArgsConstructor
public class NewsFeedLikeRepositoryImpl implements NewsFeedLikeRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	@Override
	public List<LikeUserResponseDto> findLikeUsersByNewsId(Long newsId) {
		QNewsFeedLikeEntity like = QNewsFeedLikeEntity.newsFeedLikeEntity;
		QUserEntity user = QUserEntity.userEntity;

		return queryFactory
			.select(Projections.constructor(LikeUserResponseDto.class, user.nickname))
			.from(like)
			.join(like.user, user)
			.where(like.newsFeed.id.eq(newsId))
			.fetch();
	}

	@Override
	public Page<NewsFeedPageResponseDto> findLikedFeedsByUserId(Long userId, Pageable pageable) {
		QNewsFeedLikeEntity like = QNewsFeedLikeEntity.newsFeedLikeEntity;
		QUserEntity user = QUserEntity.userEntity;
		QNewsFeedEntity news = QNewsFeedEntity.newsFeedEntity;
		QCommentEntity comment = QCommentEntity.commentEntity;
		QNewsFileEntity file = QNewsFileEntity.newsFileEntity;

		List<NewsFeedPageResponseDto> content = queryFactory
			.select(Projections.constructor(
				NewsFeedPageResponseDto.class,
				news.id,
				news.user.id,
				news.user.nickname,
				news.title,
				news.content,
				news.updatedAt,
				like.countDistinct(),
				comment.countDistinct(),
				file.path.min() // 생성자에는 조회수 있는데 컬럼에는 없음
			))
			.from(like)
			.join(like.newsFeed, news)
			.join(news.user, user)
			.leftJoin(comment).on(comment.newsFeed.eq(news))
			.leftJoin(file).on(file.newsFeed.eq(news))
			.where(like.user.id.eq(userId))
			.groupBy(news.id)
			.orderBy(news.updatedAt.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		long total = queryFactory
			.select(news.countDistinct())
			.from(like)
			.join(like.newsFeed, news)
			.where(like.user.id.eq(userId))
			.fetchOne();

		return new PageImpl<>(content, pageable, total);
	}
}
