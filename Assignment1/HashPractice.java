import org.bouncycastle.jce.provider.BouncyCastleProvider;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.util.Random;

public class HashPractice {
    public static void main(String[] args) throws NoSuchAlgorithmException {
        Security.addProvider(new BouncyCastleProvider());
        MessageDigest SHA1 = MessageDigest.getInstance("SHA-1");
        //내 학번
        String stuNum = "20192944";
        //학번 byte[]
        byte[] stuNumBytes = Utils.toByteArray(stuNum);
        //난수
        int randomInt = 0;
        //난수 byte[]
        byte[] randomBytes = new byte[16];

        //조건에 만족하는 난수를 찾을 때까지
        while(true){
            Random random = new Random();
            //난수 생성
            randomInt = random.nextInt();
            //학번 + 난수
            String combinedString = stuNum + randomInt;
            //combinedString byte[] 변환
            byte[] combinedBytes = Utils.toByteArray(combinedString);
            //update & digest
            SHA1.update(combinedBytes);
            byte[] myHash = SHA1.digest();


            if (checkCondition1(myHash)) {
                System.out.println("조건 1 : 해시값의 첫 번째 바이트가 0이 되게 하는 난수 찾기");
                System.out.println("myRandomNumber : " + randomInt);
                System.out.println("length : " + myHash.length);
                System.out.print("Hash bytes : ");
                for(byte b : myHash)
                    System.out.print(b + " ");
                System.out.println();
                System.out.println("hash hex : " + Utils.toHexString(myHash));
                break;
            }
//            if (checkCondition2(myHash)) {
//                System.out.println("조건 2 : 해시값의 첫 번째, 두 번째 바이트가 0이 되게 하는 난수 찾기");
//                System.out.println("myRandomNumber : " + randomInt);
//                System.out.println("length : " + myHash.length);
//                System.out.print("Hash bytes : ");
//                for(byte b : myHash)
//                    System.out.print(b + " ");
//                System.out.println();
//                System.out.println("hash hex : " + Utils.toHexString(myHash));
//                break;
//            }
        }
    }
    private static boolean checkCondition1(byte[] hash) {
        //첫 byte = 0
        return hash[0] == 0 ;
    }
    private static boolean checkCondition2(byte[] hash) {
        //첫 byte, 두번째 byte = 0
        return hash[0] == 0 && hash[1] == 0 ;
    }

}