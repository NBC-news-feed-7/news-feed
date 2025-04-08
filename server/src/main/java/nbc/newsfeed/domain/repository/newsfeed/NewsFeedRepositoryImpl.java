package nbc.newsfeed.domain.repository.newsfeed;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import nbc.newsfeed.domain.dto.newsfeeddto.NewsFeedResponseDto;
import nbc.newsfeed.domain.dto.newsfeeddto.NewsFeedSortType;
import nbc.newsfeed.domain.entity.QCommentEntity;
import nbc.newsfeed.domain.entity.QNewsFeedEntity;
import nbc.newsfeed.domain.entity.QNewsFeedLikeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

@RequiredArgsConstructor
public class NewsFeedRepositoryImpl implements NewsFeedRepositoryCustom {

    private final JPAQueryFactory queryFactory;


    @Override
    public Page<NewsFeedResponseDto> findFeedsWithSort(NewsFeedSortType sortType, Pageable pageable) {
        QNewsFeedEntity news = QNewsFeedEntity.newsFeedEntity;
        QNewsFeedLikeEntity like = QNewsFeedLikeEntity.newsFeedLikeEntity;
        QCommentEntity comment = QCommentEntity.commentEntity;

        List<NewsFeedResponseDto> content = queryFactory
                .select(Projections.constructor(
                        NewsFeedResponseDto.class,
                        news.id,
                        news.user.id,
                        news.user.nickname,
                        news.title,
                        news.content,
                        news.createdAt,
                        news.updatedAt,
                        like.countDistinct(),
                        comment.countDistinct()
                ))
                .from(news)
                .leftJoin(like).on(like.newsFeed.eq(news))
                .leftJoin(comment).on(comment.newsFeed.eq(news))
                .groupBy(news.id)
                .orderBy(sortType.getOrderBy(news, like))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .select(news.countDistinct())
                .from(news)
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }
}
