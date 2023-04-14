
import java.io.FileInputStream;
import java.security.Security;
import java.security.MessageDigest;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class FileHash {
	
	public static void main(String args[]) throws Exception
	{
		
		Security.addProvider(new BouncyCastleProvider());

		
		int BUF_SIZE = 2048;
		
		byte[] buffer = new byte[BUF_SIZE];
		MessageDigest digest = MessageDigest.getInstance("MD5");
//		MessageDigest digest = MessageDigest.getInstance("SHA256","BC");
		
		
		FileInputStream fis = new FileInputStream("libs/01.pdf");
		
		int read = BUF_SIZE;

		while ((read = fis.read(buffer, 0, BUF_SIZE)) > 0) {

			digest.update(buffer, 0, read);
		}
		
		
		byte[] out = digest.digest();

		
		System.out.println("md = " + Utils.toHexString(out));

	}

}
