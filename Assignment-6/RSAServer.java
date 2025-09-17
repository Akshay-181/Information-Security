import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyPair;

public class RSAServer {
    public static void main(String[] args) throws Exception {
        KeyPair kp = RSAUtil.generateKeyPair(2048);
        System.out.println("RSA Server started on port 5000...");
        try (ServerSocket server = new ServerSocket(5000)) {
            while (true) { 
                try (Socket s = server.accept();
                     BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
                     PrintWriter out = new PrintWriter(s.getOutputStream(), true)) {

                    out.println(RSAUtil.publicKeyToBase64(kp.getPublic()));

                    String cipherB64 = in.readLine();
                    if (cipherB64 == null) continue;

                    String msg = RSAUtil.decryptFromBase64(kp.getPrivate(), cipherB64);
                    System.out.println("Received: " + msg);

                    String reply = "Hello client, I got your message!";
                    String replyCipher = RSAUtil.encryptToBase64(kp.getPublic(), reply);
                    out.println(replyCipher);
                }
            }
        }
    }
}
