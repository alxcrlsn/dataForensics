/**
 * Programming Project 4 - COMP 2555
 * Created by Alex Carlson on 10/23/14.
 * This project attempts to retrieve Jpeg files from allocated disk space.
 */

import java.io.File;
import java.io.RandomAccessFile;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class ImageCarver {

    // Converts a byte array to an integer.
    public static int byteArray2Int(byte b[], int start, int end) {
        int value = 0;
        for (int i = start; i <= end; i++) {
            value += (b[i] & 0x000000FF) * Math.pow(16, 2 * (i - start));

        }
        return value;
    }

    // Saves specified bytes as a jpeg file.
    public static void saveJpeg(byte[] bytes, int start, int end, int imageCount) throws Exception {
        String fileName = "Image ";
        int length = end - start;
        FileOutputStream output =  new FileOutputStream(fileName + imageCount);
        output.write(bytes, start, length);
        output.close();
    }

    public static void main(String[] args) throws Exception {

        // Run the program only if arguments are specified.
        if (args.length == 1) {
            File theFile = new File(args[0]);
            RandomAccessFile fileRAF = new RandomAccessFile(theFile, "r");

            // Reads the entire file into a byte array.
            byte[] fileBytes = new byte[(int) fileRAF.length()];
            fileRAF.read(fileBytes);

            // Variables that the program needs to run.
            int headerStart = 0;
            int footerStart = 0;
            int headerEnd = 3;
            int footerEnd = 2;
            int imageCount = 0;
            ArrayList<Integer> headers = new ArrayList<Integer>();
            ArrayList<Integer> footers = new ArrayList<Integer>();

            // Prints the column titles for output.
            System.out.println("File name" + "\t" + "Header" + "\t" + "Footer");


            // This while loop creates an ArrayList of headers.
            while (headerEnd <= fileBytes.length) { //TODO

                if (byteArray2Int(fileBytes, headerStart, headerEnd - 1) == (0xFFD8FF)) {

                    headers.add(headerStart);
                }

                headerStart++;
                headerEnd++;
            }

            // This while loop creates an ArrayList of footers.
            while (footerEnd <= fileBytes.length) {

                if (byteArray2Int(fileBytes, footerStart, footerEnd - 1) == (0xD9FF)) {


                    footers.add(footerStart);
                }

                footerStart++;
                footerEnd++;

            }

            // Runs through the list of headers to match footers.
            for (int i=0; i < headers.size(); i++) {

                // Creates variables for the loop.
                int youShallNotPass;
                int theIndexWhichYouShallNotPass = 0;
                int h1;
                int f1 = 0;



                // This sets the the header address that the footer for the image should not pass.
                if(i + 2 < headers.size()) {
                    youShallNotPass = headers.get(i + 2);

                    for (int j = 0; j < footers.size(); j++) {
                        if (footers.get(j) >= headers.get(i + 2)) {
                            theIndexWhichYouShallNotPass = j;
                            break;
                        } else {
                            theIndexWhichYouShallNotPass = footers.size() - 1;
                        }
                    }

                // If there are no more headers, the header that should not be passed is the last header.
                }else {
                    youShallNotPass = footers.get(footers.size() - 1);
                    theIndexWhichYouShallNotPass = footers.size() - 1;
                }


                if(i +1 < headers.size()) {
                    h1 = headers.get(i + 1);
                }else {
                    h1 = headers.get(headers.size() - 1);
                }

                // Matches the footer to the correct header.
                for (int j = 0; j <= theIndexWhichYouShallNotPass; j++) {

                    if (footers.get(j) > h1) {
                        f1 = footers.get(j);
                        break;
                    }
                }

                // Saves non-thumbnail jpegs and prints relevant information to the console.
                saveJpeg(fileBytes, headers.get(i), footers.get(theIndexWhichYouShallNotPass), imageCount);
                System.out.println("Image  " + imageCount + "\t" + headers.get(i) + "\t" + footers.get(theIndexWhichYouShallNotPass));
                imageCount++;

                if(f1 == 0) {
                    f1 = footers.get(footers.size() - 1);
                }

                // Saves thumbnail jpegs and prints relevant information to the console.
                saveJpeg(fileBytes, h1, f1, imageCount);
                System.out.println("Image  " + imageCount + "\t" + h1 + "\t" + f1);
                imageCount++;
            }
        } else {
            System.out.println("No file found. Please specify a file to analyze as a command line argument.");
        }
    }
}














