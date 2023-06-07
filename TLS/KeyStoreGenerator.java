import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;

import java.io.FileOutputStream;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class KeyStoreGenerator {

    public static void main(String[] args) throws Exception {
    	
        // Add the Bouncy Castle provider for cryptographic operations
        Security.addProvider(new BouncyCastleProvider());

        // Generate RSA key pair using Bouncy Castle provider
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA", "BC");
        keyPairGenerator.initialize(2048, new SecureRandom());
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        // Set the validity period for the certificate (from current time to 1 year later)
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime expiryDate = startDate.plusYears(1);
        BigInteger serialNumber = BigInteger.valueOf(System.currentTimeMillis());

        
        // Set the distinguished name (DN) of the certificate issuer
        X500Name dnName = new X500Name("CN=localhost");
        
        // Create a certificate builder with the issuer, serial number, validity period, DN, and public key
        JcaX509v3CertificateBuilder certBuilder = new JcaX509v3CertificateBuilder(
                dnName, 
                serialNumber, 
                Date.from(startDate.atZone(ZoneId.systemDefault()).toInstant()), 
                Date.from(expiryDate.atZone(ZoneId.systemDefault()).toInstant()), 
                dnName, 
                keyPair.getPublic());

        
        // Prepare the private key and create the self-signed certificate with the signature
        ContentSigner contentSigner = new JcaContentSignerBuilder("SHA256WithRSAEncryption").build(keyPair.getPrivate());
        X509CertificateHolder certHolder = certBuilder.build(contentSigner);
        Certificate selfSignedCert = new JcaX509CertificateConverter().getCertificate(certHolder);

        // Create the key store and store the key pair and the certificate
        KeyStore keyStore = KeyStore.getInstance("JKS");
        keyStore.load(null, null);
        keyStore.setKeyEntry("server", keyPair.getPrivate(), "keystorePassword".toCharArray(), new Certificate[]{selfSignedCert});

        
        // Save the key store to a file
        try (FileOutputStream fos = new FileOutputStream("keystore.jks")) {
            keyStore.store(fos, "keystorePassword".toCharArray());
        }

        // Create the trust store, and store the certificate.
        KeyStore trustStore = KeyStore.getInstance("JKS");
        trustStore.load(null, null);
        trustStore.setCertificateEntry("server", selfSignedCert);

        // Save the trust store to a file
        try (FileOutputStream fos = new FileOutputStream("truststore.jks")) {
            trustStore.store(fos, "truststorePassword".toCharArray());
        }
    }
}
