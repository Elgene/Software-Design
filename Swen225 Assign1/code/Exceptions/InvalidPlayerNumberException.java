package Exceptions;

public class InvalidPlayerNumberException extends GameException{
    public InvalidPlayerNumberException(String errorMessage){
        super(errorMessage);
    }
}
