import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.*;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static byte[] S = new byte[]{0x78, 0x57, (byte) 0x8e, 0x5a, 0x5d, 0x63, (byte) 0xcb, 0x06};
    private static int c = 1000;
    private static int dkLen = 16;
    public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, IllegalBlockSizeException, IOException, BadPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {
        Security.addProvider(new BouncyCastleProvider());
        //Generate HashedPW with myPBKDF1

        System.out.print("Input PW for encryption : ");
        byte[] inputPW = myPBKDF1(scanner.next(), S, c, dkLen);

        //Generate 16byte(128bit) AES KeyBytes
        byte[] keyBytes = new byte[]{
                0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07,
                0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f,
        };
        SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");

        AES128Encode("/Users/oxdjww/Desktop/inputSource.txt","/Users/oxdjww/Desktop/outputSource.enc",key,inputPW);
        AES128Decode("/Users/oxdjww/Desktop/outputSource.enc","/Users/oxdjww/Desktop/decryptedSource.txt",key,inputPW);
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
        if ((dkLen > 16 && (md.getAlgorithm().equals("MD2") | md.getAlgorithm().equals("MD5"))) |
                (dkLen > 20 && md.getAlgorithm().equals("SHA1"))) {
            System.out.println("derived key too long");
            return null;
        }

        //T_1 생성
        md.update(PS);
        //T_2 ~ T_{c-1} 생성
        for (int i = 0; i < c - 1; i++) {
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
    private static void AES128Encode(String inputPath, String outputPath, SecretKeySpec key,byte[] HashedPW) throws IOException, NoSuchPaddingException, NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
        // FileInputStream 및 BufferedInputStream 생성
        FileInputStream fileInputStream = new FileInputStream(inputPath);
        BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);

        // FileOutputStream 및 BufferedOutputStream 생성
        FileOutputStream fileOutputStream = new FileOutputStream(outputPath);
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);

        //Generate 16byte(128bit) CBC Initial Vector
        byte[] ivBytes = new byte[]{
                0x07, 0x06, 0x05, 0x04, 0x03, 0x02, 0x01, 0x00,
                0x07, 0x06, 0x05, 0x04, 0x03, 0x02, 0x01, 0x00
        };

        IvParameterSpec iv = new IvParameterSpec(ivBytes);

        fileOutputStream.write(Utils.concatByteArrays(HashedPW, ivBytes));

        // Cipher 초기화
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");
        cipher.init(Cipher.ENCRYPT_MODE, key,iv);

        //BufferReader로 읽어오고 1024byte씩 암호화 하기
        System.out.println("[inputSource.txt -> outputSource.enc]");
        byte[] buffer = new byte[1024];
        int bytesRead = 0;
        double progress = 0;
        while ((bytesRead = bufferedInputStream.read(buffer)) != -1) {
            byte[] encryptedBuffer = cipher.update(buffer, 0, bytesRead);
            bufferedOutputStream.write(encryptedBuffer);
            //진행상황 5% 증가시마다 출력
            progress += 1024;
            if ((progress / 10485760) * 100 % 5 == 0) {
                System.out.printf("%.0f%% 암호화 진행 중..\n", (progress / 10485760.0) * 100);
            }
        }
        //마지막 암호화 처리
        byte[] finalBuffer = cipher.doFinal();
        bufferedOutputStream.write(finalBuffer);

        bufferedOutputStream.close();
        bufferedInputStream.close();
        fileInputStream.close();
        fileOutputStream.close();
    }
    private static void AES128Decode(String inputPath, String outputPath, SecretKeySpec key,byte[] hashedPW) throws IOException, NoSuchPaddingException, NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
        // FileInputStream 및 BufferedInputStream 생성
        FileInputStream fileInputStream = new FileInputStream(inputPath);
        BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);

        // FileOutputStream 및 BufferedOutputStream 생성
        FileOutputStream fileOutputStream = new FileOutputStream(outputPath);
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);

        byte[] headerPW = new byte[16];
        fileInputStream.read(headerPW, 0, headerPW.length);

        System.out.print("Input PW for decryption : ");
        byte[] inputPW = myPBKDF1(scanner.next(), S, c, dkLen);

        if(Arrays.equals(hashedPW,inputPW) == false){
            System.out.println("Incorrect PW..");
            return;
        } else {
            System.out.println("Correct PW..");

            //Generate 16byte(128bit) CBC Initial Vector from outputSource.enc
            byte[] ivBytes = new byte[16];
            bufferedInputStream.read(ivBytes, 0, ivBytes.length);
            IvParameterSpec iv = new IvParameterSpec(ivBytes);

            // Cipher 초기화
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");
            cipher.init(Cipher.DECRYPT_MODE, key, iv);

            // (HashedPW||IV) 부분 skip 되어있음
            //BufferReader로 읽어오고 1024byte씩 복호화 하기
            System.out.println("[outputSource.enc -> decryptedSource.txt]");
            byte[] buffer = new byte[1024];
            int bytesRead = 0;
            double progress = 0;
            while ((bytesRead = bufferedInputStream.read(buffer)) != -1) {
                byte[] decryptedBuffer = cipher.update(buffer, 0, bytesRead);
                bufferedOutputStream.write(decryptedBuffer);
                //진행상황 5% 증가시마다 출력
                progress += 1024;
                if ((progress / 10485760) * 100 % 5 == 0) {
                    System.out.printf("%.0f%% 복호화 진행 중..\n", (progress / 10485760.0) * 100);
                }
            }
            //마지막 복호화 블록 처리
            byte[] finalBuffer = cipher.doFinal();
            bufferedOutputStream.write(finalBuffer);

            bufferedOutputStream.close();
            bufferedInputStream.close();
            fileInputStream.close();
            fileOutputStream.close();
        }
    }
}