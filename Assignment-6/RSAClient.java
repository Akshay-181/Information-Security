import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.PublicKey;
import java.util.Scanner;
import java.util.Base64;

public class RSAClient {
    public static void main(String[] args) throws Exception {
        try (Socket s = new Socket("127.0.0.1", 5000);
             BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
             PrintWriter out = new PrintWriter(s.getOutputStream(), true);
             Scanner sc = new Scanner(System.in)) {

            String serverPubB64 = in.readLine();
            PublicKey serverPub = RSAUtil.publicKeyFromBase64(serverPubB64);

            String message = "Hello RSA over sockets";
            System.out.print("Message [" + message + "]: ");
            String input = sc.nextLine();
            if (!input.trim().isEmpty()) message = input;

            String cipherB64 = RSAUtil.encryptToBase64(serverPub, message);
            out.println(cipherB64);

            String replyCipher = in.readLine();
            if (replyCipher != null) {
                byte[] decodedReply = Base64.getDecoder().decode(replyCipher);
                System.out.println("Encrypted reply (Base64): " + replyCipher);
                System.out.println("Decrypted reply (server re-encrypted with public key): " +
                        new String(decodedReply)); // NOTE: cannot decrypt since client has no private key
            }
        }
    }
}
