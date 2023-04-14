import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;

public class HashTest {
    public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchProviderException {
        Security.addProvider(new BouncyCastleProvider());
        MessageDigest hash = MessageDigest.getInstance("SHA1", "BC");

        String hash_input1 = "example1";
        String hash_input2 = "MyNameis";
        String hash_input3 = "JungTae";

        hash.update(Utils.toByteArray(hash_input1));

        byte[] output1 = hash.digest();

        System.out.println("output 1 : " + Utils.toHexString(output1));

        hash.update(Utils.toByteArray(hash_input2));

        byte[] output2 = hash.digest();

        System.out.println("output 2 : " + Utils.toHexString(output2));

        hash.update(Utils.toByteArray(hash_input2));
        hash.update(Utils.toByteArray(hash_input3));

        byte[] output3 = hash.digest();

        System.out.println("output 3 : " + Utils.toHexString(output3));

    }
}