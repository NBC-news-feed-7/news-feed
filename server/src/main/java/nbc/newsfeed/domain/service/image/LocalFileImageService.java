package nbc.newsfeed.domain.service.image;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import nbc.newsfeed.domain.entity.NewsFeedEntity;
import nbc.newsfeed.domain.entity.NewsFileEntity;
import nbc.newsfeed.domain.entity.UserEntity;
import nbc.newsfeed.domain.repository.newsfeed.NewsFeedRepository;
import nbc.newsfeed.domain.repository.newsfile.NewsFileRepository;
import nbc.newsfeed.domain.repository.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import nbc.newsfeed.common.error.CustomException;
import nbc.newsfeed.common.error.ErrorCode;

/**
 * @author    : kimjungmin
 * Created on : 2025. 4. 8.
 */
@Service
public class LocalFileImageService implements ImageService {
    private final UserRepository userRepository;
    private final NewsFeedRepository newsFeedRepository;
    private final NewsFileRepository newsFileRepository;

    public LocalFileImageService(UserRepository userRepository, NewsFeedRepository newsFeedRepository, NewsFileRepository newsFileRepository) {
        this.userRepository = userRepository;
        this.newsFeedRepository = newsFeedRepository;
        this.newsFileRepository = newsFileRepository;
    }

    @Override
    public String uploadImage(MultipartFile file) {
        try {
            String originFileName = file.getOriginalFilename();

            String extension = originFileName.substring(originFileName.lastIndexOf(".") + 1);

            String fileName = UUID.randomUUID() + "." + extension;
            // TODO 환경 변수 분리 예정
            Path uploadPath = Paths.get("uploads/user/profile-images").toAbsolutePath().normalize();
            Files.createDirectories(uploadPath);

            Path filePath = uploadPath.resolve(fileName);
            file.transferTo(filePath);

            return "uploads/user/profile-images/" + fileName;
        } catch (IOException e) {
            throw new CustomException(ErrorCode.IMAGE_SAVE_FAIL);
        }
    }


    @Override
    public List<String> uploadNewsFeedImages(List<MultipartFile> files, Long userId, Long feedId) {
        if (files == null) {
            return null;
        }
        UserEntity findUser = userRepository.findById(userId).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        NewsFeedEntity findFeed = newsFeedRepository.findById(feedId).orElseThrow(() -> new CustomException(ErrorCode.NEWSFEED_NOT_FOUND));
        try {
            for (MultipartFile file : files) {
                String originalName = file.getOriginalFilename();
                String extension = originalName.substring(originalName.lastIndexOf(".") + 1);
                String fileName = UUID.randomUUID() + "." + extension;
                // 사용자 및 피드별 경로 지정
                Path uploadPath = Paths.get("uploads/user/" + userId + "/feeds/" + feedId).toAbsolutePath().normalize();
                Files.createDirectories(uploadPath); // 경로 없으면 생성

                Path filePath = uploadPath.resolve(fileName); // 전체 경로 설정
                file.transferTo(filePath); // 실제 파일 저장
                //상대경로
                String relativePath = "uploads/user/" + userId + "/feeds/" + feedId + "/" + fileName;
                NewsFileEntity fileEntity = NewsFileEntity.builder()
                        .originalName(originalName)
                        .path(relativePath)
                        .size(file.getSize())
                        .newsFeed(findFeed)
                        .user(findUser).build();
                newsFileRepository.save(fileEntity);
            }
        } catch (IOException | RuntimeException e) {
            throw new CustomException(ErrorCode.IMAGE_SAVE_FAIL);
        }
        return newsFileRepository.findAllByNewsFeed(findFeed).stream().map(NewsFileEntity::getPath).toList();
    }
}
