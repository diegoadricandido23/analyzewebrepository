package br.com.diego.checkwebrepository.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController("apiErrorHandler")
@ControllerAdvice
public class ApiErrorHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiErrorHandler.class);

    private static final String CONTENT_TYPE = "application/json; charset=utf-8";

    private HttpHeaders headersResponse;

    public ApiErrorHandler() {
        headersResponse = new HttpHeaders();
        headersResponse.setContentType(MediaType.valueOf(CONTENT_TYPE));
    }

    @ExceptionHandler(value = { CheckPageCustomException.class})
    public ResponseEntity<?> handleCheckPageCustomException(CheckPageCustomException ex) {
        LOGGER.warn(ex.getMessage());
        return new ResponseEntity<>(new ErrorResponse(ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    /**
     * Handle {@link ConstraintViolationException}
     *
     * @param request the request
     * @param ex      the exception
     * @return BadRequest
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<List<ErrorResponse>> handleConstraintViolationException(final HttpServletRequest request, final ConstraintViolationException ex) {
        LOGGER.warn(ex.getMessage());
        ArrayList<ConstraintViolation<?>> constraintViolations = new ArrayList<>(ex.getConstraintViolations());
        List<ErrorResponse> errors = constraintViolations
                .stream()
                .map(c -> new ErrorResponse(c.getMessage()))
                .collect(Collectors.toList());

        return new ResponseEntity<>(errors, headersResponse, HttpStatus.BAD_REQUEST);
    }
}
