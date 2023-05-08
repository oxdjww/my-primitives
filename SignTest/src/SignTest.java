import java.io.FileInputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.interfaces.RSAPublicKey;



public class SignTest {
	
	public static int BUF_SIZE=4096;
	RSAPrivateCrtKey privKey;
	RSAPublicKey pubKey;

	
	public void init() throws Exception {

		/* Generate a RSA key pair */
		KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
		SecureRandom random = SecureRandom.getInstance("SHA1PRNG");

		keyGen.initialize(1024, random);

		KeyPair pair = keyGen.generateKeyPair();

		privKey = (RSAPrivateCrtKey)pair.getPrivate();
		pubKey = (RSAPublicKey)pair.getPublic();
	}

	public byte[] testSign(String fileName) throws Exception {

		Signature sign = Signature.getInstance("SHA1withRSA");

		sign.initSign(privKey);

		byte[] buffer = new byte[BUF_SIZE];
		FileInputStream fis = new FileInputStream(fileName);
		int read = BUF_SIZE;

		while ((read = fis.read(buffer, 0, BUF_SIZE)) > 0) {

			sign.update(buffer);
		}

		byte[] bytesSign = sign.sign();

		return bytesSign;
	}
	
	
	public boolean testVerify(String fileName, byte[] bytesSign) throws Exception {

		Signature sign = Signature.getInstance("SHA1withRSA");

		sign.initVerify(pubKey);

		byte[] buffer = new byte[BUF_SIZE];
		FileInputStream fis = new FileInputStream(fileName);
		int read = BUF_SIZE;

		while ((read = fis.read(buffer, 0, BUF_SIZE)) > 0) {

			sign.update(buffer);
		}
		return sign.verify(bytesSign);
	}
	
	public static void main(String[] args) {

		 SignTest signTest = new SignTest();
		 try {
		   signTest.init();

		   System.out.println("Sign");
		   byte[] bytesSign = signTest.testSign("rcs/01.pdf");
		   System.out.println("Sign : " +Utils.toHexString(bytesSign));
		   
		   System.out.println("Verification");
		   if (signTest.testVerify("rcs/01.pdf", bytesSign))
		     System.out.println("Success");
		   else
		     System.out.println("fail");

		 } catch (Exception e) {
		   e.printStackTrace();
		 }

	}


		

}
