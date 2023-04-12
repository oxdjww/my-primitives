import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;

public class PBKDF1_Practice {
    public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchProviderException {
        //과제#2 명세서대로 변수들 할당
        String P = "password";
        byte[] S = new byte[]{0x78, 0x57, (byte) 0x8e, 0x5a, 0x5d, 0x63, (byte) 0xcb, 0x06};
        int c = 1000;
        byte[] DK = new byte[]{(byte)0xdc, 0x19, (byte)0x84, 0x7e, 0x05, (byte)0xc6, 0x4d, 0x2f, (byte)0xaf, 0x10, (byte)0xeb, (byte)0xfb, 0x4a, 0x3d, 0x2a, 0x20};
        int dkLen = DK.length;

        //구현한 PBKDF1로 구해진 my derived key 할당
        byte[] myDK = myPBKDF1(P, S, c, dkLen);

        //조건대로 MD2, MD5, SHA1경우에 dkLen 길이가 초과됐을 시 null 반환하여 프로그램이 종료되도록 구현
        if( myDK == null) {
            return;
        }else {
            System.out.println("myDK    : " + Utils.bytesToHex(myDK));
            System.out.println("Test DK : " + Utils.bytesToHex(DK));
        }
    }

    private static byte[] myPBKDF1(String P, byte[] S, int c, int dkLen) throws NoSuchAlgorithmException, NoSuchProviderException {
        Security.addProvider(new BouncyCastleProvider());
        MessageDigest md = MessageDigest.getInstance("SHA1", "BC");

        //PS 병합
        byte[] PS = new byte[P.length() + S.length];
        byte[] bytesPassword = Utils.toByteArray(P);
        System.arraycopy(bytesPassword, 0, PS, 0, bytesPassword.length);
        System.arraycopy(S, 0, PS, bytesPassword.length, S.length);

        //종료조건
        if((dkLen > 16 && (md.getAlgorithm().equals("MD2")| md.getAlgorithm().equals("MD5")))|
                (dkLen > 20 && md.getAlgorithm().equals("SHA1")))
        {
            System.out.println("derived key too long");
            return null;
        }

        //T_1 생성
        md.update(PS);
        //T_2 ~ T_{c-1} 생성
        for(int i = 0 ; i < c-1 ; i++)
        {
            byte[] temp = md.digest();
            md.update(temp);
        }
        //T_c생성
        byte[] output = md.digest();

        //비교를 위해 추출 후 새로운 byte 배열에 할당
        byte[] myDK = new byte[dkLen];
        System.arraycopy(output, 0, myDK, 0, dkLen);

        return myDK;
    }
}