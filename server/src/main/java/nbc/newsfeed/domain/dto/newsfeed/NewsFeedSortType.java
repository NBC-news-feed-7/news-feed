package nbc.newsfeed.domain.dto.newsfeeddto;

import com.querydsl.core.types.OrderSpecifier;
import nbc.newsfeed.domain.entity.QNewsFeedEntity;
import nbc.newsfeed.domain.entity.QNewsFeedLikeEntity;

public enum NewsFeedSortType {
    LATEST {
        @Override
        public OrderSpecifier<?> getOrderBy(QNewsFeedEntity news, QNewsFeedLikeEntity like) {
            return news.createdAt.desc();
        }
    },
    OLDEST {
        @Override
        public OrderSpecifier<?> getOrderBy(QNewsFeedEntity news, QNewsFeedLikeEntity like) {
            return news.createdAt.asc();
        }
    },
    MOST_LIKES {
        @Override
        public OrderSpecifier<?> getOrderBy(QNewsFeedEntity news, QNewsFeedLikeEntity like) {
            return like.count().desc();
        }
    };

    public abstract OrderSpecifier<?> getOrderBy(QNewsFeedEntity news, QNewsFeedLikeEntity like);
}
