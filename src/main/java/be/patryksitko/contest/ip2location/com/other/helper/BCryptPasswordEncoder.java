package be.patryksitko.contest.ip2location.com.other.helper;

import be.patryksitko.contest.ip2location.com.other.helper.exception.PasswordFormatException;

public interface BCryptPasswordEncoder {

    static final org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder getInstance = new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder();

    static boolean isPasswordMeetingStandards(String password) throws PasswordFormatException {
        if (!password.matches("^.{8,}$")) {
            throw new PasswordFormatException("Password must contain atleast 8 characters.");
        }
        if (!password.matches("^(?=.*[a-z]).*$")) {
            throw new PasswordFormatException("Password must include lowercase letter.");
        }
        if (!password.matches("^(?=.*[A-Z]).*$")) {
            throw new PasswordFormatException("Password must include uppercase letter.");
        }
        if (!password.matches("^(?=.*[0-9]).*$")) {
            throw new PasswordFormatException("Password must include digit.");
        }
        if (!password.matches("^(?=.*[!@#$%^&*]).*$")) {
            throw new PasswordFormatException("Password must include special character.");
        }
        return true;
    }

}
