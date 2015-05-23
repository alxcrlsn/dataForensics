import java.util.ArrayList;

/**
 * Created by alxcrlsn on 11/14/14.
 */
public class Test {
    public static void main (String args[]) {

        ArrayList<Integer> someBytes = new ArrayList<Integer>();
        someBytes.add(1);
        someBytes.add(0);
        someBytes.add(1);
        someBytes.add(1);

        System.out.println(someBytes);

        binaryToByte(someBytes);

    }


    public static byte[] binaryToByte (ArrayList<Integer> someBinary) {
        byte[] bytesToReturn = new byte[(someBinary.size())/8];
        String binaryToByte = "";

        for (int i = 0; i < someBinary.size(); i++) {

            System.out.println(someBinary.get(i));

            binaryToByte += (Integer.toString(someBinary.get(i)));

        }

        System.out.println("String "+ binaryToByte);
        return bytesToReturn;
    }
}
