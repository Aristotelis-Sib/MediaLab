package customExceptions;

public class OversizeException extends Exception{
    public OversizeException(String errorMessage){
        super(errorMessage);
    }
}
