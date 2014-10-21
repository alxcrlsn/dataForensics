/**
 * COMP 2555 -- Assignment 3
 * Created by Alex Carlson on 10/14/2014
 * Linux memory dump and describes all running processes.
 * Program takes a file path as a command line argument.
 */


import java.io.*;


public class RamPreview {

    //Function given to us via cs.du.edu/2555
    public static long byteArray2Long(byte b[], int start, int end) {
        long value = 0;
        for (int i = start; i <= end; i++) {
            value += (b[i] & 0x000000FF) * Math.pow(16, 2 * (i - start));
            //or, value += (b[i] & 0x000000FF) << (8*(i-start));
        }
        return value;
    }

    //Prints output in columns
    public static void toString (long pid, long ppid, long uid, String task, String comm) {
        System.out.println(pid + "\t" + ppid + "\t" + uid + "\t" + task + "\t" + "\t" + "\t" + comm);

    }

    //Prints each byte in a byte array up to specified length
    public static void printByteArray(byte[] bytes, int length) {
        for(int i = 0; i < length; i++) {
            System.out.print(Integer.toHexString(bytes[i] & 0x000000FF));
        }

        System.out.println("\n");
    }

    //Prints a single byte in a byte array
    public static void printByteInArray(byte[] bytes, int theByte) {
        System.out.println(Integer.toHexString(bytes[theByte] & 0x000000FF));
    }


    //Main method runs the program6
    public static void main(String[] args) throws Exception {

        boolean run = true;
        long pid;
        long ppid;
        long uid;
        long vmz;
        long parentStart;
        int task;
        int startPointer = (0xC0660BC0 - 0xC0000000);
        int pointerLocation = (startPointer);
        String comm;
        String taskToPrint;
        byte[] parentProcessID = new byte[4];
        byte[] nextProcess = new byte[4];
        byte[] bytesToName = new byte[16];
        byte[] processDescriptor = new byte[1024];


        //Runs program if arguments are specified. If not, prints error message.
        if (args.length == 1) {

            File forAnalysis = new File(args[0]);
            RandomAccessFile theFile = new RandomAccessFile(forAnalysis, "r");

            //Prints column headers
            System.out.println("PID" + "\t" + "PPID" + "\t" + "UID" + "\t" + "TASK" + "\t" + "\t" + "\t" + "\t" + "COMM");


            while(run) {

                //Reads process descriptor into a 1024 byte array
                theFile.seek(pointerLocation);
                theFile.read(processDescriptor);

                // Sets TASK
                task = pointerLocation + 0xC0000000;
                taskToPrint = Integer.toHexString(task);

                //Sets PID
                pid = (byteArray2Long(processDescriptor, 0xA8, 0xA8 + 0x03));

                //Sets UID
                uid = (byteArray2Long(processDescriptor, 0x14C, 0x14C + 0x03));

                //Sets COMM
                theFile.seek(pointerLocation + 0x194);
                theFile.read(bytesToName);
                byte[] processName = new byte[16];

                //Removes extraneous characters from the name
                for (int i = 0; i < bytesToName.length - 1; i ++) {
                    if (bytesToName[i] == 0x0) {
                        break;
                    } else {
                        processName[i] = bytesToName[i];
                    }
                }
                comm = (new String(processName, "UTF-8"));

                //Looks up Parent Process ID
                parentStart = byteArray2Long(processDescriptor, 0xB0, 0xB0 + 0x04);
                theFile.seek((parentStart - 0xC0000000) + 0xA8);
                theFile.read(parentProcessID);
                ppid = (byteArray2Long(parentProcessID, 0, 3));

                //Finds next process
                theFile.seek(pointerLocation + 0x7C);
                theFile.read(nextProcess);

                toString(pid, ppid, uid, taskToPrint, comm);

                pointerLocation = (int) (byteArray2Long(nextProcess, 0, 3) - 0xC0000000 - 0x7C);

                //Ends the loop if the current pointer matches the start pointer
                if (pointerLocation == startPointer) {
                    run = false;
                }
            }

        } else {

            System.out.println("File not found. Please specify the file to analyze as a command line argument.");
        }
    }
}