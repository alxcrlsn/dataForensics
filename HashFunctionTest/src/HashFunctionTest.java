import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.util.Formatter;

/**
 * Created by alxcrlsn on 9/17/14.
 */
public class HashFunctionTest {

    public static String calculateHash(MessageDigest algorithm,
                                       String fileName) throws IOException {

        FileInputStream fileInputStr = new FileInputStream(fileName);
        BufferedInputStream bufferedInputStr = new BufferedInputStream(fileInputStr);
        DigestInputStream digestInputStr = new DigestInputStream(bufferedInputStr, algorithm);

        // read the file and update the hash calculation
        while (digestInputStr.read() != -1);

        // get the hash value as byte array
        byte[] hashValue = algorithm.digest();

        return byteToHexArray(hashValue);
    }

    private static String byteToHexArray(byte[] hashArray) {
        Formatter formatter = new Formatter();
        for (byte b : hashArray) {
            formatter.format("%02x", b);
        }
        return formatter.toString();
    }

    public static void main(String[] args) throws Exception {

        System.out.println(calculateHash(MessageDigest.getInstance("SHA1"), "test.txt"));
        System.out.println(calculateHash(MessageDigest.getInstance("MD5"), "test.txt"));
    }
}
