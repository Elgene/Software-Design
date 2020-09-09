package Exceptions;

public class InvalidNameException extends GameException{
   public InvalidNameException(String errorMessage){
       super(errorMessage);
   }
}
