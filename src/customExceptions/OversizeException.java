package customExceptions;

public class OversizeException extends RuntimeException{
    public OversizeException(String errorMessage){
        super(errorMessage);
    }
}
