package nbc.newsfeed.domain.repository.newsfeed;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import nbc.newsfeed.domain.dto.newsfeeddto.NewsFeedResponseDto;
import nbc.newsfeed.domain.dto.newsfeeddto.NewsFeedSortType;
import nbc.newsfeed.domain.entity.QCommentEntity;
import nbc.newsfeed.domain.entity.QNewsFeedEntity;
import nbc.newsfeed.domain.entity.QNewsFeedLikeEntity;

import java.util.List;

@RequiredArgsConstructor
public class NewsFeedRepositoryImpl implements NewsFeedRepositoryCustom {

    private final JPAQueryFactory queryFactory;


    @Override
    public List<NewsFeedResponseDto> findFeedsWithSort(NewsFeedSortType sortType) {
        QNewsFeedEntity news = QNewsFeedEntity.newsFeedEntity;
        QNewsFeedLikeEntity like = QNewsFeedLikeEntity.newsFeedLikeEntity;
        QCommentEntity comment = QCommentEntity.commentEntity;

        return queryFactory
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
                .where(news.createdAt.between(start, end))
                .groupBy(news.id)
                .orderBy(sortType.getOrderBy(news, like))
                .fetch();
    }
}
