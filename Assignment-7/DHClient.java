import java.io.*;
import java.net.*;
import java.math.BigInteger;
import java.security.SecureRandom;

public class DHClient {
    public static void main(String[] args) throws Exception {
        Socket socket = new Socket("127.0.0.1", 5000);
        System.out.println("Connected to server.");

        DataInputStream in = new DataInputStream(socket.getInputStream());
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());

        BigInteger p = new BigInteger(in.readUTF());
        BigInteger g = new BigInteger(in.readUTF());
        BigInteger A = new BigInteger(in.readUTF());

        SecureRandom random = new SecureRandom();
        BigInteger b = new BigInteger(1024, random).mod(p);
        BigInteger B = g.modPow(b, p); 

        out.writeUTF(B.toString());

        BigInteger sharedSecret = A.modPow(b, p);

        System.out.println("Shared secret (client): " + sharedSecret);

        socket.close();
    }
}
