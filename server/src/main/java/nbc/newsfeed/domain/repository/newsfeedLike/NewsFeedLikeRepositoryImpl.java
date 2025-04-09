package nbc.newsfeed.domain.repository.newsfeedLike;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import nbc.newsfeed.domain.dto.newsfeed.NewsFeedPageResponseDto;
import nbc.newsfeed.domain.dto.newsfeedLike.LikeUserResponseDto;
import nbc.newsfeed.domain.entity.QCommentEntity;
import nbc.newsfeed.domain.entity.QNewsFeedEntity;
import nbc.newsfeed.domain.entity.QNewsFeedLikeEntity;
import nbc.newsfeed.domain.entity.QUserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

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
        QNewsFeedEntity news = QNewsFeedEntity.newsFeedEntity;
        QCommentEntity comment = QCommentEntity.commentEntity;

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
                        comment.countDistinct()
                ))
                .from(like)
                .join(like.newsFeed, news)
                .leftJoin(comment).on(comment.newsFeed.eq(news))
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
