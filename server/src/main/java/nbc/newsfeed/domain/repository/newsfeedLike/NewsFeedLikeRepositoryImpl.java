package nbc.newsfeed.domain.repository.newsfeedLike;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import nbc.newsfeed.domain.dto.newsfeedLike.LikeUserResponseDto;
import nbc.newsfeed.domain.entity.QNewsFeedLikeEntity;
import nbc.newsfeed.domain.entity.QUserEntity;

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
}
