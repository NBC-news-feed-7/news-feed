package nbc.newsfeed.common.error;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	// ✅ 1. CustomException 처리
	@ExceptionHandler(CustomException.class)
	public ResponseEntity<ErrorResponseDto> handleCustomException(CustomException ex, HttpServletRequest request) {
		return ResponseEntity
			.status(ex.getErrorCode().getStatus())
			.body(ErrorResponseDto.from(ex.getErrorCode(), request.getRequestURI()));
	}

	// ✅ 2. @Valid 검증 실패 처리
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponseDto> handleValidationException(MethodArgumentNotValidException ex,
		HttpServletRequest request) {
		return ResponseEntity
			.badRequest()
			.body(ErrorResponseDto.from(ex, request.getRequestURI()));
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponseDto> handleInternalException(Exception ex,
		HttpServletRequest request) {
		log.error("internal server error: {}", ex.getMessage(), ex);

		return ResponseEntity
			.badRequest()
			.body(ErrorResponseDto.from(ErrorCode.INTERNAL_SERVER_ERROR, request.getRequestURI()));
	}
	//3명박 이상 들어올시
	@ExceptionHandler(MaxUploadSizeExceededException.class)
	public ResponseEntity<ErrorResponseDto> handleMaxSizeException(MaxUploadSizeExceededException ex,
																   HttpServletRequest request) {
		return ResponseEntity
				.badRequest()
				.body(ErrorResponseDto.from(ex, request.getRequestURI()));
	}
}