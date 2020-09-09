package Exceptions;

public class InvalidMovementException extends GameException {
    public InvalidMovementException (String errorMessage){
        super(errorMessage);
    }
}
