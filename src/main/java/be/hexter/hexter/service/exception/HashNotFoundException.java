package be.hexter.hexter.service.exception;

public class HashNotFoundException extends RuntimeException{
    
    public HashNotFoundException(Integer hash){
        super("Hash (" + hash + ") available."); 
    }
}
