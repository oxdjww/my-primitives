
import java.security.MessageDigest;
import java.security.Security;

import jdk.jshell.execution.Util;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
public class HashTest {

	public static void main(String args[]) throws Exception
	{

	    Security.addProvider(new BouncyCastleProvider());
	    
	    MessageDigest hash = MessageDigest.getInstance("SHA256", "BC");
	
		String student_no="12345678";
		String student_department="software";
		String student_name="hongildong";

		String ex = "12345678softwarehongildong";

		String ssu = "Soongil Univ";

		hash.update(Utils.toByteArray(ssu));

		// ä�� ����
		// A ���: 1.1, 1.2, 2 �� ���
		// B ���: �ܼ� �Ǽ�
		// C �[��: 3�� ����� ������ update Ȥ�� digest�Լ� ��� �̼�
		// D ���: ������
		// ���� ������ ���, ���� �Ͻø� ����� ������ ������
		
		// 1.1 ��õ�ϴ� ��� (1)
		hash.update(Utils.toByteArray(student_no));
		hash.update(Utils.toByteArray(student_department));
		hash.update(Utils.toByteArray(student_name));

		hash.update(Utils.toByteArray(ex));

		hash.digest();

		byte[] out1 = hash.digest();

		byte[] out2 = hash.digest((Utils.toByteArray(ssu)));
h
		byte[] out3 = hash.digest((Utils.toByteArray(ex)));
				
		// 1.2 ��õ�ϴ� ��� (2)
		//hash.update(Utils.toByteArray(student_no));
		//hash.update(Utils.toByteArray(student_department));
				
		//byte[] out = hash.digest(Utils.toByteArray(student_name));
		
		
		// 2. ��õ���� �ʴ� ��� (����� ����)
		//hash.update(Utils.toByteArray(student_no+student_department+student_name));
		//byte[] out = hash.digest();
		
		
		
		// 3. �߸��� ����� ����: update�� digest�Լ��� input�� �ߺ����� �Է���
		//hash.update(Utils.toByteArray(student_no+student_department+student_name));
		//byte[] out = hash.digest(Utils.toByteArray(student_no+student_department+student_name));

//		System.out.println("Hash : " + Utils.toHexString(ex));
		System.out.println("Hash : " + Utils.toHexString(out1));
		System.out.println("Hash size : " + hash.getDigestLength());
		System.out.println("Algo : " + hash.getAlgorithm());

	}
}
