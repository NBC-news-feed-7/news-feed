package nbc.newsfeed.domain.repository.newsfeed;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import nbc.newsfeed.domain.dto.newsfeed.NewsFeedPageResponseDto;
import nbc.newsfeed.domain.dto.newsfeed.NewsFeedSortType;
import nbc.newsfeed.domain.entity.QCommentEntity;
import nbc.newsfeed.domain.entity.QNewsFeedEntity;
import nbc.newsfeed.domain.entity.QNewsFeedLikeEntity;
import nbc.newsfeed.domain.entity.QNewsFileEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RequiredArgsConstructor
public class NewsFeedRepositoryImpl implements NewsFeedRepositoryCustom {

    private final JPAQueryFactory queryFactory;


    @Override
    public Page<NewsFeedPageResponseDto> searchFeeds(String keyword, LocalDate startDate, LocalDate endDate, NewsFeedSortType sortType, Pageable pageable) {
        QNewsFeedEntity news = QNewsFeedEntity.newsFeedEntity;
        QNewsFeedLikeEntity like = QNewsFeedLikeEntity.newsFeedLikeEntity;
        QCommentEntity comment = QCommentEntity.commentEntity;
        QNewsFileEntity file = QNewsFileEntity.newsFileEntity;

        BooleanBuilder where = new BooleanBuilder();
        // deleteAt 추가
        where.and(news.deletedAt.isNull());

        if (keyword != null && !keyword.isBlank()) {
            where.and(news.title.containsIgnoreCase(keyword)
                    .or(news.content.containsIgnoreCase(keyword)));
        }
        if (startDate != null) {
            where.and(news.createdAt.goe(startDate.atStartOfDay()));
        }
        if (endDate != null) {
            where.and(news.createdAt.loe(endDate.atTime(LocalTime.MAX)));
        }

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
                        file.path.min(),
                        news.viewCount
                ))
                .from(news)
                .leftJoin(like).on(like.newsFeed.eq(news))
                .leftJoin(comment).on(comment.newsFeed.eq(news))
                .leftJoin(file).on(file.newsFeed.eq(news))
                .where(where)
                .groupBy(news.id)
                .orderBy(sortType.getOrderBy(news, like))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .select(news.countDistinct())
                .from(news)
                .where(where)
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<NewsFeedPageResponseDto> searchFriendFeeds(List<Long> friendIds, NewsFeedSortType sortType, Pageable pageable) {
        if (friendIds.isEmpty()) return Page.empty(pageable);

        QNewsFeedEntity news = QNewsFeedEntity.newsFeedEntity;
        QNewsFeedLikeEntity like = new QNewsFeedLikeEntity("like");
        QCommentEntity comment = new QCommentEntity("comment");
        QNewsFileEntity file = QNewsFileEntity.newsFileEntity;

        BooleanBuilder where = new BooleanBuilder();
        where.and(news.user.id.in(friendIds));
        where.and(news.deletedAt.isNull());

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
                        file.path.min(),
                        news.viewCount
                ))
                .from(news)
                .leftJoin(like).on(like.newsFeed.eq(news))
                .leftJoin(comment).on(comment.newsFeed.eq(news))
                .leftJoin(file).on(file.newsFeed.eq(news))
                .where(where)
                .groupBy(news.id)
                .orderBy(sortType.getOrderBy(news, like))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .select(news.count())
                .from(news)
                .where(where)
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }

}
