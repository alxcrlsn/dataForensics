/**
 * COMP 2555 -- Assignment 2
 * Created by Alex Carlson on 10/3/2014
 * Reads NTFS volume and prints file information.
 * Program takes a file path as a command line argument.
 */

import java.io.*;

public class DiskPreview {

    //Function given to us via cs.du.edu/2555
    public static int byteArray2Int (byte b[], int start, int end) {
        int value = 0;
        for (int i=start; i<=end; i++) {
            value += (b[i] & 0x000000FF) * Math.pow(16,2*(i-start));
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

           //Creates byte arrays for the boot sector and MFT entries.
           byte[] bootSector = new byte[512];
           byte[] mft = new byte[1024];

           //Reads boot sector to see how drive is configured.
           dis.read(bootSector);

           //Calculates how many bytes/sector, sectors/cluster, and MFT start cluster.
           //Also calculates cluster size and how far one must skip to reach the mft.
           int bytesPerSector = byteArray2Int(bootSector, 11, 12);
           int sectorsPerCluster = byteArray2Int(bootSector, 13, 13);
           int mftStartCluster = byteArray2Int(bootSector, 0x30, 0x37);

           int clusterSize = bytesPerSector * sectorsPerCluster;
           int skipToMFT = ((clusterSize * mftStartCluster) - 512);

           //Variables for print statement.
           String fileName;
           int NDS = 0;
           int CN = mftStartCluster;
           int FCBN = 0;
           int OFF = 0;

           //Skips to beginning of MFT and reads 1024 bytes for analysis.
           dis.skipBytes(skipToMFT);

           while (dis.read(mft) != -1) {

               /**
               While loop looks for MFT records using the 46 49 4C 45 hex identifier.
               It converts the first 4 bytes of each 1024 byte block to an int
               and compares it against the int value of 45 4C 49 46 (Little-endian)
               to determine a valid entry. Non-valid record entries are discarded.
               */

               if (byteArray2Int(mft, 0, 3) == 1162627398) {

                   fileName = null;
                   int dataRunAttributeCount = 0;
                   int attributeLocator = byteArray2Int(mft, 0x14, 0x14);

                   //Looks for end of record.
                   while(((mft[attributeLocator] & 0x000000FF) != 0xff)) {

                       int attributeSize = byteArray2Int(mft, attributeLocator + 0x04, attributeLocator + 0x05);

                       //Looks for file name.
                       if (byteArray2Int(mft, attributeLocator, attributeLocator) == 48) {
                           NDS = 0;
                           byte[] fileNameToPrint = new byte[attributeSize - 0x5a];

                               int fileNameStart = attributeLocator + 0x5a;
                               int fileNameEnd = attributeLocator + attributeSize;

                           //Converts the byte array to ASCII
                           for (int i = 0; i < attributeSize - 0x5a; i++) {
                               if (fileNameStart < fileNameEnd) {
                                   fileNameToPrint[i] = mft[fileNameStart];
                                   fileNameStart++;
                               }
                           }

                           //Creates string with file name.
                           String innerFileName =((new String(fileNameToPrint, "UTF-8")));

                           //Passes the string outside of the if statement to be printed.
                           fileName = innerFileName;

                       }


                       //Looks for data stream.
                       if (byteArray2Int(mft, attributeLocator, attributeLocator) == 128) {

                           dataRunAttributeCount++;
                           int dataStart;

                           //Looks for resident files.
                           if((mft[attributeLocator + 0x08] == 0x00 && dataRunAttributeCount == 1)) {

                               dataStart = attributeLocator + 0x08;
                               dataStart = dataStart + 0x10;

                               //Prevents program from breaking if resident data contains 00. (Like with $Repair).
                               if (((mft[dataStart] & 0x000000FF) == 0x00  && dataRunAttributeCount == 1)) {

                                  FCBN = 0;

                               } else {

                                    OFF = dataStart;
                                    FCBN = CN;
                                   }
                           }

                           //Looks for non-resident files.
                           if((mft[attributeLocator + 0x08] == 0x01 && dataRunAttributeCount == 1)) {
                               
                               dataStart = (attributeLocator + 0x40);
                               int dataStartHex = (mft[attributeLocator + 0x40]);

                               //Prevents program from breaking if data run value contains FF. (Like with $Volume).
                               if (((mft[dataStart] & 0x000000FF) == 0x00 ||(mft[dataStart] & 0x000000FF) == 255 )  && dataRunAttributeCount == 1) {
                                   FCBN = 0;
                               }
                               String dataRun = Integer.toHexString(dataStartHex & 0x000000FF);
                               if (dataRun.length() == 2 && dataRunAttributeCount == 1) {
                                   int firstComponent = (Character.getNumericValue(dataRun.charAt(0)));
                                   int secondComponent = (Character.getNumericValue(dataRun.charAt(1)));

                                       FCBN = byteArray2Int(mft, dataStart + secondComponent + 1, dataStart + secondComponent + firstComponent);
                               }

                               OFF = 0;
                           }

                           //Increments data stream counter after each data stream.
                           NDS++;

                       }
                       //Increments the attribute locator to the next attribute.
                       attributeLocator = attributeLocator + attributeSize;
                   }

                   //Prints the output
                   if(NDS >= 1 && fileName != null) {
                       if ((NDS - 1) <= 0) {
                           System.out.println(fileName + ":\n          " + CN + " :: " + FCBN + "(+" + OFF + ")\n");
                       } else {
                           System.out.println(fileName + ":\n          " + CN + " :: " + FCBN + "(+" + OFF + ") [:: " + (NDS - 1) + "]\n");
                       }
                   }

                   CN += (mft.length/clusterSize);
               }

           }
        }

        else {
            System.out.println("File not found. Please specify the file to analyze as a command line argument.");
        }
    }
}
