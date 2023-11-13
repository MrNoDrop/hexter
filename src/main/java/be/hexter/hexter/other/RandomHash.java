package be.hexter.hexter.other;

import java.security.SecureRandom;
import java.math.BigInteger;

public class RandomHash {
    private static SecureRandom random = new SecureRandom();

    public static String generateRandomString(int length){
        return new BigInteger(length * 5, random).toString(32).substring(0, length);
    }
}