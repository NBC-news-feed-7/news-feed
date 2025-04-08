package nbc.newsfeed.domain.service.image;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

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
}
