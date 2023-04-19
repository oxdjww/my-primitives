import java.math.BigInteger;
import java.security.SecureRandom;

public class MyRSAPractice {
    public static void main(String[] args) {
        MyRSAPractice myRSA = new MyRSAPractice();


    }

    private void keyGen() {
        SecureRandom sr = new SecureRandom();
        sr.setSeed(System.currentTimeMillis());
        BigInteger p = BigInteger.probablePrime(1024, sr);
        BigInteger q = BigInteger.probablePrime(1024, sr);
        BigInteger n = p.multiply(q);
//        BigInteger phiN =
    }
}