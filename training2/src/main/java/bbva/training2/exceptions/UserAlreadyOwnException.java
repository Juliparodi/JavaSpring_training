package bbva.training2.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class UserAlreadyOwnException extends RuntimeException{

    public UserAlreadyOwnException(String message){
        super(message);
    }

}
