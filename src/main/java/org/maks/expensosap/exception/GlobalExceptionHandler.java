package org.maks.expensosap.exception;

import org.maks.expensosap.dto.ApiErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<ApiErrorDTO> buildError(
            HttpStatus status,
            String message,
            WebRequest req
    ) {
        ApiErrorDTO err = ApiErrorDTO.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(message)
                .path(req.getDescription(false).replace("uri=", ""))
                .build();

        return new ResponseEntity<>(err, status);
    }

    // 1. RuntimeException — почти все наши ошибки
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiErrorDTO> handleRuntime(RuntimeException ex, WebRequest req) {
        return buildError(HttpStatus.BAD_REQUEST, ex.getMessage(), req);
    }

    // 2. Default fallback — если что-то не поймали
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorDTO> handleDefault(Exception ex, WebRequest req) {
        return buildError(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error", req);
    }
}
