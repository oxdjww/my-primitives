import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.Security;

public class Main {
    public static void main(String[] args) {
        Security.addProvider(new BouncyCastleProvider());
        String providerName = "BC";
        if(Security.getProvider(providerName) == null)
        {
            System.out.println(providerName + " provider not installed");
        }
        else
        {
            System.out.println(providerName + " is installed");
        }
    }
}