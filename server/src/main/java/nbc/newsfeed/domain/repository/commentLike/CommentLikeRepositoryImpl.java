package nbc.newsfeed.domain.repository.commentLike;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import nbc.newsfeed.domain.dto.newsfeedLike.LikeUserResponseDto;
import nbc.newsfeed.domain.entity.QCommentLikeEntity;
import nbc.newsfeed.domain.entity.QUserEntity;

import java.util.List;

@RequiredArgsConstructor
public class CommentLikeRepositoryImpl implements CommentLikeRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<LikeUserResponseDto> findLikeUsersByCommentId(Long newsId) {
        QCommentLikeEntity like = QCommentLikeEntity.commentLikeEntity;
        QUserEntity user = QUserEntity.userEntity;

        return queryFactory
                .select(Projections.constructor(LikeUserResponseDto.class, user.nickname))
                .from(like)
                .join(like.user, user)
                .where(like.comment.id.eq(newsId))
                .fetch();
    }
}
