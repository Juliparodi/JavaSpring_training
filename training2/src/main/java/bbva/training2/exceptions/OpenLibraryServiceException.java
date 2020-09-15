package bbva.training2.exceptions;

public class OpenLibraryServiceException extends RuntimeException {

    public OpenLibraryServiceException(String message) {
        super(message);
    }

    public OpenLibraryServiceException(String m, Throwable t) {
        super(m, t);
    }


}
