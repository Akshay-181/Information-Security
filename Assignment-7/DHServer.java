import java.io.*;
import java.net.*;
import java.math.BigInteger;
import java.security.SecureRandom;

public class DHServer {
    public static void main(String[] args) throws Exception {
        BigInteger p = new BigInteger("23");  
        BigInteger g = new BigInteger("5");

        SecureRandom random = new SecureRandom();
        BigInteger a = new BigInteger(1024, random).mod(p); 
        BigInteger A = g.modPow(a, p); 

        ServerSocket serverSocket = new ServerSocket(5000);
        System.out.println("Server started. Waiting for client...");

        Socket socket = serverSocket.accept();
        System.out.println("Client connected.");

        DataInputStream in = new DataInputStream(socket.getInputStream());
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());

        out.writeUTF(p.toString());
        out.writeUTF(g.toString());
        out.writeUTF(A.toString());

        String clientPublicKey = in.readUTF();
        BigInteger B = new BigInteger(clientPublicKey);

        BigInteger sharedSecret = B.modPow(a, p);

        System.out.println("Shared secret (server): " + sharedSecret);

        socket.close();
        serverSocket.close();
    }
}
