/**
 * COMP 2555 -- Assignment 3
 * Created by Alex Carlson on 10/14/2014
 * Linux memory dump and describes all running processes.
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

    public static byte[] readFromFile(RandomAccessFile someFile, long position, int size) throws Exception {

        someFile.seek(position);
        byte[] theBytes = new byte[size];
        someFile.read(theBytes);
        return theBytes;
    }


    public static void main(String[] args) throws Exception {

        //Runs program if arguments are specified. If not, prints error message.
        if (args.length == 1) {

            File forAnalysis = new File(args[0]);
            RandomAccessFile theFile = new RandomAccessFile(forAnalysis, "r");

            int descriptorSize;

            theFile.skipBytes(0xC0660BC0 - 0xC0000000);
            System.out.println(theFile.getFilePointer());
            theFile.seek(0x7C);

            byte[] theBytes = readFromFile(theFile, theFile.getFilePointer(), 4);

            for (int i = 0; i < 4; i++) {
                System.out.println(theBytes[i]);
            }

            System.out.println(byteArray2Int(theBytes, 0, 3));






            //System.out.println(args[0]);
            //System.out.println(theFile);

        } else {
            System.out.println("File not found. Please specify the file to analyze as a command line argument.");
        }
    }
}