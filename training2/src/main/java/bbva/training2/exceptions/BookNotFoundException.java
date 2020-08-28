package bbva.training2.exceptions;

import org.springframework.http.HttpStatus;

public class BookNotFoundException extends RuntimeException{


    public BookNotFoundException(String message, Throwable err){
        super(message, err);
    }

    public BookNotFoundException(String message){
        super(message);
    }

    public BookNotFoundException(){
        super();
    }

}
