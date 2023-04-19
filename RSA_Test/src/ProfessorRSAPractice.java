import java.math.BigInteger;
import java.security.SecureRandom;

public class ProfessorRSAPractice {
    public static void main(String[] args) {

        SecureRandom sr = new SecureRandom();
        sr.setSeed(System.currentTimeMillis());

        // 512 bits p, q
        BigInteger p = BigInteger.probablePrime(512, sr);
        BigInteger q = BigInteger.probablePrime(512, sr);

        // n = p * q
        BigInteger n = p.multiply(q);

        // check
        System.out.println(n.toString());

        // pi(n)
        BigInteger pi_n = (p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE)));
        System.out.println("pi(n) : " + pi_n.toString());

        // e, d
        BigInteger e = BigInteger.valueOf(65537);
        System.out.println("e : " + e.toString());

        BigInteger d = e.modInverse(pi_n);

        // encryption
        BigInteger plainText = new BigInteger("1234");
        BigInteger cipherText = plainText.modPow(e, n);

        System.out.println("plainText = " + plainText.toString());
        System.out.println("cipherText = " + cipherText.toString());

        // decryption
        BigInteger dm = cipherText.modPow(d, n);
        System.out.println("dm : " + dm.toString());
    }
}
