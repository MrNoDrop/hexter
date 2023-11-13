package be.hexter.hexter.service.exception;

public class UsergroupNotFoundException extends RuntimeException {
    
    public UsergroupNotFoundException(String firstname, String lastname){
        super("There are not users matching: "+firstname+", "+lastname+".");
    }
}
