import java.io.*;
import java.net.*;
import java.security.MessageDigest;

public class Receiver {
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
        try (ServerSocket serverSocket = new ServerSocket(6000)) {
            System.out.println("Receiver is waiting for sender...");

            Socket socket = serverSocket.accept();
            System.out.println("Sender connected.");
            DataInputStream in = new DataInputStream(socket.getInputStream());
            String receivedMessage = in.readUTF();
            String receivedHash = in.readUTF();
            System.out.println("📩 Received message: " + receivedMessage);
            System.out.println("📩 Received hash: " + receivedHash);
            String computedHash = getHash(receivedMessage, "SHA-256");
            System.out.println("🔑 Computed hash: " + computedHash);

            if (receivedHash.equals(computedHash)) {
                System.out.println("✅ Integrity check passed. Message is not altered.");
            } else {
                System.out.println("❌ Integrity check failed. Message may be altered.");
            }

            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
