package customExceptions;

public class OverlapTilesException extends RuntimeException{
    public OverlapTilesException(String errorMesage){
        super(errorMesage);
    }
}
