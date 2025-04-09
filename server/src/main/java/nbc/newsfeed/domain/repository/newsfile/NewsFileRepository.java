package nbc.newsfeed.domain.repository.newsfile;

import nbc.newsfeed.domain.entity.NewsFeedEntity;
import nbc.newsfeed.domain.entity.NewsFileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NewsFileRepository extends JpaRepository<NewsFileEntity, Long> {

    List<NewsFileEntity> findAllByNewsFeed(NewsFeedEntity newsFeed);

    List<NewsFileEntity> findAllByNewsFeed_Id(Long newsFeedId);
}
