package bbva.training2.exceptions;


import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@ControllerAdvice
@Slf4j
public class AppExceptionHandler extends ResponseEntityExceptionHandler {


    @ExceptionHandler({NullPointerException.class})
    public ResponseEntity<String> handleAnyNullException(NullPointerException ex) {
        log.error("NUllPointerException ---- {}", Throwables.getStackTraceAsString(ex));
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({BookAlreadyOwnException.class, UserAlreadyOwnException.class})
    public ResponseEntity<String> handleAnyOwnException(RuntimeException ex) {
        log.error("User/Book AlreadyOwnException--- {}", Throwables.getStackTraceAsString(ex));
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler({BookNotFoundException.class, UserNotFoundException.class})
    public ResponseEntity<String> handleAnyNotFoundException(RuntimeException ex) {
        log.info("BookNotFoundException---- {}", Throwables.getStackTraceAsString(ex));
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<String> handleAnyException(Exception ex) {
        log.error("UnexpectedException: {}", Throwables.getStackTraceAsString(ex));
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
