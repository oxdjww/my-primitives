import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.Provider;
import java.security.Security;

public class ProviderTest {
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

        System.out.println("-------------------------------------------");

        Provider[] providers = Security.getProviders();
        for(int i = 0 ; i < providers.length ; i++){
            Provider provider = providers[i];
            System.out.println("Provider name : " + provider.getName());
            System.out.println("Provider info : " + provider.getInfo()+"\n");
        }
    }
}