package customExceptions;

public class OverlapTilesException extends Exception{
    public OverlapTilesException(String errorMesage){
        super(errorMesage);
    }
}
