package customExceptions;

public class InvalidCountExeception extends  RuntimeException{
    public InvalidCountExeception(String errorMesage){
        super(errorMesage);
    }
}
