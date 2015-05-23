/**
 * Assignment 1 - COMP 2555
 * Created by Alex Carlson on 9/15/14. Submitted 9/18/14.
 * Computes MD5 and SHA-1 hash values for a file or string of text.
 */

//Imports necessary for project to work.
import java.security.*;
import java.io.*;
import java.util.Formatter;
import javax.xml.bind.annotation.adapters.HexBinaryAdapter;

/**
 * The Hash class contains two methods. calculateTextHash calculates the SHA-1 or MD5 value of a string of text.
 * calculateFileHash calculates the SHA-1 or MD5 value of a file. The file must be in the project folder. See "text.txt".
 */

public class Hash {

    // Calculates the SHA-1 and MD5 value of a string.
    public static String calculateTextHash(MessageDigest algorithm, String text) throws Exception {

        // Converts message into bytes.
        byte[] bytesOfMessage = text.getBytes("UTF-8");

        MessageDigest md = algorithm;

        // Runs hashing algorithm on bytes of message.
        byte[] theDigest = md.digest(bytesOfMessage);

        HexBinaryAdapter adapter = new HexBinaryAdapter();

            // Converts binary to hex.
            return adapter.marshal(theDigest);

    }

    // Calculates the SHA-1 or MD5 value for a file.
    public static String calculateFileHash(MessageDigest algorithm, String fileName) throws Exception {

            // Opens a file in the project folder, i.e. test.txt
            FileInputStream fileInputStr = new FileInputStream(fileName);
            BufferedInputStream bufferedInputStr = new BufferedInputStream(fileInputStr);
            DigestInputStream digestInputStr = new DigestInputStream(bufferedInputStr, algorithm);

            // Reads and digests file
            while (digestInputStr.read() != -1);

            // Generates hash value as a byte array.
            byte[] hashValue = algorithm.digest();

            return byteToHexArray(hashValue);
        }

    // Converts a byte array to a hex array.
    private static String byteToHexArray(byte[] hashArray) {
        Formatter formatter = new Formatter();
        for (byte b : hashArray) {
            formatter.format("%02x", b);
        }
        return formatter.toString();
    }

    // Main/test driver executes the program.
    public static void main(String[] args) throws Exception {

        // This section runs the program. Specify file name and algorithm here.
        System.out.println("MD5 and SHA-1 checksums for string: This program computes the SHA1 and MD5 checksums" + "\n");
        System.out.println("String MD5 Hash: " + calculateTextHash(MessageDigest.getInstance("MD5"), "This program computes the SHA1 and MD5 checksums"));
        System.out.println("String SHA-1 Hash: " + calculateTextHash(MessageDigest.getInstance("SHA-1"), "This program computes the SHA1 and MD5 checksums!") + "\n");

        System.out.println("MD5 and SHA-1 checksums for file: test.txt" + "\n");
        System.out.println("File MD5 Hash: " + calculateFileHash(MessageDigest.getInstance("MD5"), "test.txt"));
        System.out.println("File SHA-1 Hash: " + calculateFileHash(MessageDigest.getInstance("SHA-1"), "test.txt"));

    }
}
