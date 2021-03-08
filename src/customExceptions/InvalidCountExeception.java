package customExceptions;

public class InvalidCountExeception extends  Exception{
    public InvalidCountExeception(String errorMesage){
        super(errorMesage);
    }
}
