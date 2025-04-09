package nbc.newsfeed.domain.service.image;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author    : kimjungmin
 * Created on : 2025. 4. 8.
 */
public interface ImageService {
	String uploadImage(MultipartFile file);
	List<String> uploadNewsFeedImages(List<MultipartFile> files, Long userId, Long feedId);
}
