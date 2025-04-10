package nbc.newsfeed;

import nbc.newsfeed.domain.dto.newsfeed.NewsFeedRequestDto;
import nbc.newsfeed.domain.entity.NewsFeedEntity;
import nbc.newsfeed.domain.entity.UserEntity;
import nbc.newsfeed.domain.repository.newsfeed.NewsFeedRepository;
import nbc.newsfeed.domain.repository.user.UserRepository;
import nbc.newsfeed.domain.service.newsfeed.NewsFeedService;
import nbc.newsfeed.domain.service.user.UserService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest
class NewsFeedViewCountTests {

    @Autowired
    private NewsFeedService newsFeedService;
    @Autowired
    private NewsFeedRepository newsFeedRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;

    @Test
    void contextLoads() {
    }


    //조회수 동시성 테스트
    @BeforeEach
    public void before(){
        UserEntity user = userRepository.findById(1L).orElseThrow();
        newsFeedService.createNewsFeed(user.getId(), NewsFeedRequestDto.builder()
                .title("테스트케이스")
                .content("동시성테스트")
                .build());
    }
    @AfterEach
    public void after(){
        newsFeedRepository.deleteAll();
    }

    @Test
    public void viewCountJust() throws InterruptedException {
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        for(int i = 0; i < threadCount; i++){
            executorService.submit(()->{
                try {
                    newsFeedService.getNewsFeed(1L);
                }finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
        NewsFeedEntity findNewsFeed = newsFeedRepository.getByIdOrThrow(1L);
        Assertions.assertThat(findNewsFeed.getViewCount()).isEqualTo(100);
    }

    @Test //락걸기
    public void viewCount() throws InterruptedException {
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        for(int i = 0; i < threadCount; i++){
            executorService.submit(()->{
                try {
                    newsFeedService.getNewsFeedPessimistic(1L);
                }finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
        NewsFeedEntity findNewsFeed = newsFeedRepository.getByIdOrThrow(1L);
        Assertions.assertThat(findNewsFeed.getViewCount()).isEqualTo(100);

    }

}
