import java.util.Scanner;

public class DES {

    // Initial Permutation (IP) table
    private static final int[] IP = {
        58, 50, 42, 34, 26, 18, 10, 2,
        60, 52, 44, 36, 28, 20, 12, 4,
        62, 54, 46, 38, 30, 22, 14, 6,
        64, 56, 48, 40, 32, 24, 16, 8,
        57, 49, 41, 33, 25, 17, 9, 1,
        59, 51, 43, 35, 27, 19, 11, 3,
        61, 53, 45, 37, 29, 21, 13, 5,
        63, 55, 47, 39, 31, 23, 15, 7
    };

    // Final Permutation (IP^-1) table
    private static final int[] FP = {
        40, 8, 48, 16, 56, 24, 64, 32,
        39, 7, 47, 15, 55, 23, 63, 31,
        38, 6, 46, 14, 54, 22, 62, 30,
        37, 5, 45, 13, 53, 21, 61, 29,
        36, 4, 44, 12, 52, 20, 60, 28,
        35, 3, 43, 11, 51, 19, 59, 27,
        34, 2, 42, 10, 50, 18, 58, 26,
        33, 1, 41, 9, 49, 17, 57, 25
    };

    // Expansion (E) table
    private static final int[] E = {
        32, 1, 2, 3, 4, 5,
        4, 5, 6, 7, 8, 9,
        8, 9, 10, 11, 12, 13,
        12, 13, 14, 15, 16, 17,
        16, 17, 18, 19, 20, 21,
        20, 21, 22, 23, 24, 25,
        24, 25, 26, 27, 28, 29,
        28, 29, 30, 31, 32, 1
    };
    
    // Permutation (P) table for the Feistel function
    private static final int[] P = {
        16, 7, 20, 21, 29, 12, 28, 17,
        1, 15, 23, 26, 5, 18, 31, 10,
        2, 8, 24, 14, 32, 27, 3, 9,
        19, 13, 30, 6, 22, 11, 4, 25
    };

    // S-Boxes (Substitution Boxes)
    private static final int[][][] S_BOXES = {
        { // S1
            {14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7},
            {0, 15, 7, 4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8},
            {4, 1, 14, 8, 13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0},
            {15, 12, 8, 2, 4, 9, 1, 7, 5, 11, 3, 14, 10, 0, 6, 13}
        },
        { // S2
            {15, 1, 8, 14, 6, 11, 3, 4, 9, 7, 2, 13, 12, 0, 5, 10},
            {3, 13, 4, 7, 15, 2, 8, 14, 12, 0, 1, 10, 6, 9, 11, 5},
            {0, 14, 7, 11, 10, 4, 13, 1, 5, 8, 12, 6, 9, 3, 2, 15},
            {13, 8, 10, 1, 3, 15, 4, 2, 11, 6, 7, 12, 0, 5, 14, 9}
        },
        { // S3
            {10, 0, 9, 14, 6, 3, 15, 5, 1, 13, 12, 7, 11, 4, 2, 8},
            {13, 7, 0, 9, 3, 4, 6, 10, 2, 8, 5, 14, 12, 11, 15, 1},
            {13, 6, 4, 9, 8, 15, 3, 0, 11, 1, 2, 12, 5, 10, 14, 7},
            {1, 10, 13, 0, 6, 9, 8, 7, 4, 15, 14, 3, 11, 5, 2, 12}
        },
        { // S4
            {7, 13, 14, 3, 0, 6, 9, 10, 1, 2, 8, 5, 11, 12, 4, 15},
            {13, 8, 11, 5, 6, 15, 0, 3, 4, 7, 2, 12, 1, 10, 14, 9},
            {10, 6, 9, 0, 12, 11, 7, 13, 15, 1, 3, 14, 5, 2, 8, 4},
            {3, 15, 0, 6, 10, 1, 13, 8, 9, 4, 5, 11, 12, 7, 2, 14}
        },
        { // S5
            {2, 12, 4, 1, 7, 10, 11, 6, 8, 5, 3, 15, 13, 0, 14, 9},
            {14, 11, 2, 12, 4, 7, 13, 1, 5, 0, 15, 10, 3, 9, 8, 6},
            {4, 2, 1, 11, 10, 13, 7, 8, 15, 9, 12, 5, 6, 3, 0, 14},
            {11, 8, 12, 7, 1, 14, 2, 13, 6, 15, 0, 9, 10, 4, 5, 3}
        },
        { // S6
            {12, 1, 10, 15, 9, 2, 6, 8, 0, 13, 3, 4, 14, 7, 5, 11},
            {10, 15, 4, 2, 7, 12, 9, 5, 6, 1, 13, 14, 0, 11, 3, 8},
            {9, 14, 15, 5, 2, 8, 12, 3, 7, 0, 4, 10, 1, 13, 11, 6},
            {4, 3, 2, 12, 9, 5, 15, 10, 11, 14, 1, 7, 6, 0, 8, 13}
        },
        { // S7
            {4, 11, 2, 14, 15, 0, 8, 13, 3, 12, 9, 7, 5, 10, 6, 1},
            {13, 0, 11, 7, 4, 9, 1, 10, 14, 3, 5, 12, 2, 15, 8, 6},
            {1, 4, 11, 13, 12, 3, 7, 14, 10, 15, 6, 8, 0, 5, 9, 2},
            {6, 11, 13, 8, 1, 4, 10, 7, 9, 5, 0, 15, 14, 2, 3, 12}
        },
        { // S8
            {13, 2, 8, 4, 6, 15, 11, 1, 10, 9, 3, 14, 5, 0, 12, 7},
            {1, 15, 13, 8, 10, 3, 7, 4, 12, 5, 6, 11, 0, 14, 9, 2},
            {7, 11, 4, 1, 9, 12, 14, 2, 0, 6, 10, 13, 15, 3, 5, 8},
            {2, 1, 14, 7, 4, 10, 8, 13, 15, 12, 9, 0, 3, 5, 6, 11}
        }
    };
    
    // Permuted Choice 1 (PC-1) for key generation
    private static final int[] PC1 = {
        57, 49, 41, 33, 25, 17, 9,
        1, 58, 50, 42, 34, 26, 18,
        10, 2, 59, 51, 43, 35, 27,
        19, 11, 3, 60, 52, 44, 36,
        63, 55, 47, 39, 31, 23, 15,
        7, 62, 54, 46, 38, 30, 22,
        14, 6, 61, 53, 45, 37, 29,
        21, 13, 5, 28, 20, 12, 4
    };

    // Permuted Choice 2 (PC-2) for key generation
    private static final int[] PC2 = {
        14, 17, 11, 24, 1, 5, 3, 28,
        15, 6, 21, 10, 23, 19, 12, 4,
        26, 8, 16, 7, 27, 20, 13, 2,
        41, 52, 31, 37, 47, 55, 30, 40,
        51, 45, 33, 48, 44, 49, 39, 56,
        34, 53, 46, 42, 50, 36, 29, 32
    };
    
    // Left shifts for key generation
    private static final int[] SHIFTS = {1, 1, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 1};

    /**
     * Helper to perform a permutation.
     */
    private static String permute(String input, int[] table) {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < table.length; i++) {
            output.append(input.charAt(table[i] - 1));
        }
        return output.toString();
    }

    /**
     * Helper to perform a left circular shift.
     */
    private static String leftShift(String input, int n) {
        return input.substring(n) + input.substring(0, n);
    }

    /**
     * Generates 16 48-bit subkeys from the original 64-bit key.
     */
    private static String[] generateSubkeys(String key64bit) {
        // 1. Apply PC-1 to get a 56-bit key
        String key56bit = permute(key64bit, PC1);

        // 2. Split the 56-bit key into two 28-bit halves
        String c0 = key56bit.substring(0, 28);
        String d0 = key56bit.substring(28, 56);
        
        String[] subkeys = new String[16];

        for (int i = 0; i < 16; i++) {
            // 3. Perform left circular shifts on each half
            c0 = leftShift(c0, SHIFTS[i]);
            d0 = leftShift(d0, SHIFTS[i]);
            
            // 4. Combine halves and apply PC-2 to get a 48-bit subkey
            String combined = c0 + d0;
            subkeys[i] = permute(combined, PC2);
        }
        return subkeys;
    }

    /**
     * The Feistel function F(R, K).
     */
    private static String feistelFunction(String rightHalf32, String roundKey48) {
        // 1. Expand right half from 32 bits to 48 bits
        String expanded = permute(rightHalf32, E);
        
        // 2. XOR with the round key
        String xored = xor(expanded, roundKey48);
        
        // 3. S-Box substitution
        StringBuilder sboxOutput = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            String sixBitBlock = xored.substring(i * 6, i * 6 + 6);
            int row = Integer.parseInt(sixBitBlock.charAt(0) + "" + sixBitBlock.charAt(5), 2);
            int col = Integer.parseInt(sixBitBlock.substring(1, 5), 2);
            int val = S_BOXES[i][row][col];
            sboxOutput.append(String.format("%4s", Integer.toBinaryString(val)).replace(' ', '0'));
        }
        
        // 4. Final permutation (P-Box)
        return permute(sboxOutput.toString(), P);
    }
    
    /**
     * Performs the main DES algorithm (encryption or decryption).
     */
    private static String desAlgorithm(String text64bit, String[] subkeys) {
        // 1. Initial Permutation
        String permutedText = permute(text64bit, IP);

        // 2. Split into 32-bit left and right halves
        String left = permutedText.substring(0, 32);
        String right = permutedText.substring(32, 64);
        
        // 3. 16 rounds of Feistel network
        for (int i = 0; i < 16; i++) {
            String rightOld = right;
            // Apply Feistel function F(R, K)
            String feistelResult = feistelFunction(right, subkeys[i]);
            // R_i = L_{i-1} XOR F(R_{i-1}, K_i)
            right = xor(left, feistelResult);
            // L_i = R_{i-1}
            left = rightOld;
        }

        // 4. Swap left and right halves after the 16th round
        String combined = right + left;

        // 5. Final Permutation
        return permute(combined, FP);
    }

    // --- Utility Methods ---

    private static String xor(String a, String b) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < a.length(); i++) {
            result.append(a.charAt(i) ^ b.charAt(i));
        }
        return result.toString();
    }

    private static String hexToBinary(String hex) {
        StringBuilder binary = new StringBuilder();
        for (char hexChar : hex.toCharArray()) {
            int i = Integer.parseInt(String.valueOf(hexChar), 16);
            binary.append(String.format("%4s", Integer.toBinaryString(i)).replace(' ', '0'));
        }
        return binary.toString();
    }

    private static String binaryToHex(String binary) {
        StringBuilder hex = new StringBuilder();
        for (int i = 0; i < binary.length(); i += 4) {
            String fourBits = binary.substring(i, i + 4);
            int decimal = Integer.parseInt(fourBits, 2);
            hex.append(Integer.toHexString(decimal));
        }
        return hex.toString().toUpperCase();
    }
    
    // --- Public Facing Methods ---
    
    public static String encrypt(String plaintextHex, String keyHex) {
        String plaintextBin = hexToBinary(plaintextHex);
        String keyBin = hexToBinary(keyHex);
        String[] subkeys = generateSubkeys(keyBin);
        String ciphertextBin = desAlgorithm(plaintextBin, subkeys);
        return binaryToHex(ciphertextBin);
    }
    
    public static String decrypt(String ciphertextHex, String keyHex) {
        String ciphertextBin = hexToBinary(ciphertextHex);
        String keyBin = hexToBinary(keyHex);
        String[] subkeys = generateSubkeys(keyBin);
        // Reverse subkeys for decryption
        String[] reversedSubkeys = new String[16];
        for (int i = 0; i < 16; i++) {
            reversedSubkeys[i] = subkeys[15 - i];
        }
        String plaintextBin = desAlgorithm(ciphertextBin, reversedSubkeys);
        return binaryToHex(plaintextBin);
    }
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("--- DES Algorithm Demo ---");
        System.out.print("Enter 16-char HEX plaintext: ");
        String plaintext = scanner.nextLine();
        
        System.out.print("Enter 16-char HEX key:       ");
        String key = scanner.nextLine();
        
        if (plaintext.length() != 16 || key.length() != 16) {
            System.out.println("Error: Plaintext and key must be 16 hexadecimal characters long.");
            return;
        }

        System.out.println("\nOriginal Plaintext: " + plaintext);
        System.out.println("Key:                " + key);

        String ciphertext = encrypt(plaintext, key);
        System.out.println("\nEncrypted Ciphertext: " + ciphertext);

        String decryptedtext = decrypt(ciphertext, key);
        System.out.println("Decrypted Plaintext:  " + decryptedtext);

        scanner.close();
    }
}