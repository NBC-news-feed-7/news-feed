package nbc.newsfeed.common.error;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // ✅ 1. CustomException 처리
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Map<String, Object>> handleCustomException(CustomException ex, HttpServletRequest request) {
        ErrorCode errorCode = ex.getErrorCode();

        return ResponseEntity
                .status(errorCode.getStatus())
                .body(Map.of(
                        "timestamp", LocalDateTime.now(),
                        "status", errorCode.getStatus().value(),
                        "error", errorCode.getStatus().name(),
                        "message", errorCode.getMessage(),
                        "path", request.getRequestURI()
                ));
    }

    // ✅ 2. @Valid 검증 실패 처리
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(MethodArgumentNotValidException ex,
                                                                         HttpServletRequest request) {
        List<Map<String, String>> fieldErrors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> Map.of(
                        "field", error.getField(),
                        "message", Objects.requireNonNull(error.getDefaultMessage())
                )).collect(Collectors.toList());

        return ResponseEntity
                .badRequest()
                .body(Map.of(
                        "timestamp", LocalDateTime.now(),
                        "status", 400,
                        "error", "BAD_REQUEST",
                        "message", "잘못된 입력값입니다",
                        "path", request.getRequestURI(),
                        "fieldErrors", fieldErrors
                ));
    }
}