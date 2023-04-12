import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;

public class MacTest {
    public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException {
        Security.addProvider(new BouncyCastleProvider());
        String message = "mac message";
        byte key[] = new byte[]{
                0x01, 0x02, 0x03, 0x01, 0x02, 0x03, 0x01, 0x02, 0x03, 0x01, 0x02, 0x03};
        //Mac mac = Mac.getInstance("HmacSHA1", "BC");
        Mac mac = Mac.getInstance("HmacSHA256", "BC");
        Key keyHmac = new SecretKeySpec(key, "HacSHA1");
        mac.init(keyHmac);

        mac.update(Utils.toByteArray(message));
        byte[] out = mac.doFinal(Utils.toByteArray(message));
        System.out.println("MAC Length : " + mac.getMacLength());
        System.out.println("MAC= " + Utils.toHexString(out));

    }
}
