package org.maks.expensosap.exception;

import org.maks.expensosap.dto.ApiErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorDTO> handleValidation(MethodArgumentNotValidException ex, WebRequest req) {

        String msg = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .collect(Collectors.joining(", "));

        return buildError(HttpStatus.BAD_REQUEST, msg, req);
    }

    // most of errors end up here
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiErrorDTO> handleRuntime(RuntimeException ex, WebRequest req) {
        return buildError(HttpStatus.BAD_REQUEST, ex.getMessage(), req);
    }

    // if something slipped through and wasn't caught
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorDTO> handleDefault(Exception ex, WebRequest req) {
        return buildError(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error", req);
    }
}
