/**
 * COMP 2555 -- Assignment 2
 * Created by Alex Carlson on 10/3/2014
 * Reads NTFS volume and prints file information.
 * Program takes a file path as a command line argument.
 */

import java.io.*;

public class RamPreview {

    //Function given to us via cs.du.edu/2555
    public static int byteArray2Int(byte b[], int start, int end) {
        int value = 0;
        for (int i = start; i <= end; i++) {
            value += (b[i] & 0x000000FF) * Math.pow(16, 2 * (i - start));
            //or, value += (b[i] & 0x000000FF) << (8*(i-start));
        }
        return value;
    }

    public static void main(String[] args) throws Exception {

        InputStream is;
        DataInputStream dis;

        //Runs program if arguments are specified. If not, prints error message.
        if (args.length == 1) {

            is = new FileInputStream(args[0]);
            dis = new DataInputStream(is);
            System.out.println(args[0]);
        } else {
            System.out.println("File not found. Please specify the file to analyze as a command line argument.");
        }
    }
}