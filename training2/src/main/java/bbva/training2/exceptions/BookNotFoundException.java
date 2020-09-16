package bbva.training2.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class BookNotFoundException extends RuntimeException {


    public BookNotFoundException(String message, Throwable err) {
        super(message, err);
    }

    public BookNotFoundException(String message) {
        super(message);
    }

}
