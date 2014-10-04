import java.io.*;

/**
 * Created by alxcrlsn on 9/29/14.
 * Go through loop until read method returns -1. Read one record at a time (1024 bytes).
 */
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

        InputStream is = null;
        DataInputStream dis = null;

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

           String fileName = null;
           int NDS = 0;
           int CN = mftStartCluster;
           int FCBN = 0;
           int OFF = 0;
           int clusterCounter = 0;


           //Skips to beginning of MFT and reads 1024 bytes for analysis.
           dis.skipBytes(skipToMFT);
           clusterCounter = mftStartCluster;

           while (dis.read(mft) != -1) {


               /*
               Looks for MFT records using the 46 49 4C 45 hex identifier.
               I convert the first 4 bytes of each 1024 byte block to an int
               and compare it against the int value of 45 4C 49 46 (Little-endian)
               to determine a valid entry. Non-valid record entries are discarded.
               */

               if (byteArray2Int(mft, 0, 3) == 1162627398) { //if this is a valid mft entry

                   int dataRunAttributeCount = 0;

                   fileName = null;

                   int attributeLocator = byteArray2Int(mft, 0x14, 0x14);
                   //System.out.println(attributeLocator);

                   while(((mft[attributeLocator] & 0x000000FF) != 0xff)){ //run until you find the end of mft signature
                       //System.out.println(Integer.toHexString(mft[attributeLocator] & 0x000000FF));

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

                           String innerFileName =((new String(fileNameToPrint, "UTF-8")));

                           //Passes the string outside of the if statement to be printed.
                           fileName = innerFileName; //saves file name to variable accessible outside loop.

                       }


                       //Looks for data stream.
                       if (byteArray2Int(mft, attributeLocator, attributeLocator) == 128) { //if you find a data stream


                           dataRunAttributeCount++;
                           int dataStart;

                           //looks for resident file
                           if((mft[attributeLocator + 0x08] == 0x00 && dataRunAttributeCount == 1)) {


                               dataStart = attributeLocator + 0x08;
                               dataStart = dataStart + 0x10;

                               if (((mft[dataStart] & 0x000000FF) == 0x00  && dataRunAttributeCount == 1)) {

                                  FCBN = 0;

                               } else {
                                    //System.out.println(Integer.toHexString((mft[dataStart] & 0x000000FF)));
                                    OFF = dataStart;
                                    FCBN = CN;
                                   }

                               //nonresident file found
                           }
                           if((mft[attributeLocator + 0x08] == 0x01 && dataRunAttributeCount == 1)) {
                               
                               dataStart = (attributeLocator + 0x40);
                               int dataStartHex = (mft[attributeLocator + 0x40]);

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

                           NDS++; //increase the data stream counter

                       }
                       attributeLocator = attributeLocator + attributeSize;
                   }

                   if(NDS >= 1 && fileName != null) {
                       System.out.println(fileName + ":\n          " + CN + " " + clusterCounter + " :: " + FCBN + "(+" + OFF + ") [:: " + (NDS - 1) + "]\n");


                   }

                   clusterCounter += sectorsPerCluster;

                   CN = CN + 2;

                   //break;

               }

           }
        }

        else {
            System.out.println("File not found. Please specify the file to analyze as a command line argument.");
        }
    }
}

//format print statement 10 spaces in. Ex:

/*

usb.txt:
        326706 :: 326706(+280) :: 2


*/
