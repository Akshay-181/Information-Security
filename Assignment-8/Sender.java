import java.io.*;
import java.net.*;
import java.security.MessageDigest;
import java.util.Scanner;

public class Sender {
    public static String getHash(String message, String algorithm) throws Exception {
        MessageDigest digest = MessageDigest.getInstance(algorithm);
        byte[] hashBytes = digest.digest(message.getBytes("UTF-8"));

        StringBuilder hexString = new StringBuilder();
        for (byte b : hashBytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append("0");
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public static void main(String[] args) {
        try (Socket socket = new Socket("127.0.0.1", 6000);
             DataOutputStream out = new DataOutputStream(socket.getOutputStream());
             Scanner sc = new Scanner(System.in)) {
            System.out.print("Enter the message to send: ");
            String message = sc.nextLine();

            String hash = getHash(message, "SHA-256");
            out.writeUTF(message);
            out.writeUTF(hash);
            System.out.println("✅ Message and SHA-256 hash sent.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
