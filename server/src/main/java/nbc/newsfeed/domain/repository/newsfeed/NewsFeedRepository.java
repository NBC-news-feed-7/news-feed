package nbc.newsfeed.domain.repository.newsfeed;

import jakarta.persistence.LockModeType;
import nbc.newsfeed.common.error.CustomException;
import nbc.newsfeed.common.error.ErrorCode;
import nbc.newsfeed.domain.entity.NewsFeedEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsFeedRepository extends JpaRepository<NewsFeedEntity, Long>,NewsFeedRepositoryCustom{
    default NewsFeedEntity getByIdOrThrow(Long id) {
        return findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NEWSFEED_NOT_FOUND));
    }

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select n from NewsFeedEntity n where n.id =:id")
    NewsFeedEntity findByIdWithPessimisticLock(@Param("id") Long id);
}
