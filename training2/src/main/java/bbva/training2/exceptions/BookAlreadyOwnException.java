package bbva.training2.exceptions;

public class BookAlreadyOwnException extends RuntimeException {


    public BookAlreadyOwnException(String message){
       super(message);
    }

}
