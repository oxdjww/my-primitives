import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.Security;


public class CipherTest {

    public static void main(String args[]) throws Exception
    {

        Security.addProvider(new BouncyCastleProvider());

        byte[]        input = new byte[] {
                0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07,
                0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f,
                0x10, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17 };

        byte[]        keyBytes = new byte[] {
                0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07,
                0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f,
        };


        byte[]          ivBytes = new byte[] {
                0x07, 0x06, 0x05, 0x04, 0x03, 0x02, 0x01, 0x00,
                0x07, 0x06, 0x05, 0x04, 0x03, 0x02, 0x01, 0x00};

        SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");

        //AES Initial Vector assign
        IvParameterSpec iv = new IvParameterSpec(ivBytes);

        //Plain text input
        System.out.println("input : " + Utils.toHexString(input));

        System.out.println("encrypt start");

        cipher.init(Cipher.ENCRYPT_MODE, key, iv);

        byte[] output = new byte[cipher.getOutputSize(input.length)];
        int processLen;

        processLen = cipher.update(input, 0, input.length, output, 0);

        System.out.println("update : " + Utils.toHexStringC(output));

        processLen = cipher.doFinal(output, processLen);

        System.out.println("doFinal : " + Utils.toHexStringC(output));


        System.out.println("output : " + Utils.toHexString(output));

        System.out.println("encrypt end");

        System.out.println("decrypt start");

        cipher.init(Cipher.DECRYPT_MODE, key, iv);

        byte[] plainText =
                new byte[cipher.getOutputSize(output.length)];

        int decryptedLen = cipher.update(output, 0, output.length, plainText,0);
        decryptedLen += cipher.doFinal(plainText, decryptedLen);

        System.out.println("decrypt fin");
        System.out.println("original len  : " + input.length);
        System.out.println("plaintext len : " + plainText.length);
        System.out.println("original  : " + Utils.toHexString(input));
        System.out.println("plaintext : " + Utils.toHexString(plainText));

    }
}

