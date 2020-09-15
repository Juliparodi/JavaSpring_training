package bbva.training2.exceptions;

public class PublicApiServiceException extends RuntimeException {

    public PublicApiServiceException(String m, Throwable t) {
        super(m, t);
    }

}
