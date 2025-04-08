package nbc.newsfeed.domain.service.image;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author    : kimjungmin
 * Created on : 2025. 4. 8.
 */
public interface ImageService {
	String uploadImage(MultipartFile file);
}
