package bbva.training2.exceptions.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class UserHttpErrors extends Exception{

    public UserHttpErrors(String message) {
        super(message);
    }

    public void userNotFound () {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, this.getMessage());
    }

}
