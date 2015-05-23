import java.awt.image.*;
import javax.imageio.*;
import java.io.*;


public class StegoBreak {

    static String hiddenImg = "";

    private static void extractGreenBlue(BufferedImage img, int x, int y, int s) {
        String imageString = "";

        int argb = img.getRGB(x, y);    // gives the color of the pixel as a 32-bit value

        int red = (argb & 0x00FF0000) >> 16;
        int green = (argb & 0x0000FF00) >> 8;
        int blue = (argb & 0x000000FF);

        red = (red & 0x00000001);
        green = (green & 0x00000001);
        blue = (blue & 0x00000001);

        hiddenImg += "" + blue + green;
    }

    private static void extractRedBlue(BufferedImage img, int x, int y, int s) {
        String imageString = "";

        int argb = img.getRGB(x, y);    // gives the color of the pixel as a 32-bit value

        int red = (argb & 0x00FF0000) >> 16;
        int green = (argb & 0x0000FF00) >> 8;
        int blue = (argb & 0x000000FF);

        red = (red & 0x00000001);
        green = (green & 0x00000001);
        blue = (blue & 0x00000001);

        hiddenImg = "" + blue + red;
    }

    private static void extractRedGreen(BufferedImage img, int x, int y, int s) {
        String imageString = "";

        int argb = img.getRGB(x, y);    // gives the color of the pixel as a 32-bit value

        int red = (argb & 0x00FF0000) >> 16;
        int green = (argb & 0x0000FF00) >> 8;
        int blue = (argb & 0x000000FF);

        red = (red & 0x00000001);
        green = (green & 0x00000001);
        blue = (blue & 0x00000001);

        hiddenImg = "" + green + red;
    }

    private static void extractRedGreenBlue(BufferedImage img, int x, int y, int s) {
        String imageString = "";

        int argb = img.getRGB(x, y);    // gives the color of the pixel as a 32-bit value

        int red = (argb & 0x00FF0000) >> 16;
        int green = (argb & 0x0000FF00) >> 8;
        int blue = (argb & 0x000000FF);

        red = (red & 0x00000001);
        green = (green & 0x00000001);
        blue = (blue & 0x00000001);

        hiddenImg += "" + blue + green + red;
    }

    private static void extractRed(BufferedImage img, int x, int y, int s) {
        String imageString = "";

        int argb = img.getRGB(x, y);    // gives the color of the pixel as a 32-bit value

        int red = (argb & 0x00FF0000) >> 16;
        int green = (argb & 0x0000FF00) >> 8;
        int blue = (argb & 0x000000FF);

        red = (red & 0x00000001);
        green = (green & 0x00000001);
        blue = (blue & 0x00000001);

        hiddenImg = "" + red;
    }

    private static void extractGreen(BufferedImage img, int x, int y, int s) {
        String imageString = "";

        int argb = img.getRGB(x, y);    // gives the color of the pixel as a 32-bit value

        int red = (argb & 0x00FF0000) >> 16;
        int green = (argb & 0x0000FF00) >> 8;
        int blue = (argb & 0x000000FF);

        red = (red & 0x00000001);
        green = (green & 0x00000001);
        blue = (blue & 0x00000001);

        hiddenImg = "" + green;
    }

    private static void extractBlue(BufferedImage img, int x, int y, int s) {
        String imageString = "";

        int argb = img.getRGB(x, y);    // gives the color of the pixel as a 32-bit value

        int red = (argb & 0x00FF0000) >> 16;
        int green = (argb & 0x0000FF00) >> 8;
        int blue = (argb & 0x000000FF);

        red = (red & 0x00000001);
        green = (green & 0x00000001);
        blue = (blue & 0x00000001);

        hiddenImg = "" + blue;

    }

    public static void main(String args[]) throws Exception {

        if (args.length == 1) {
            int counter = 0;

            BufferedImage img = ImageIO.read(new File(args[0]));
            FileOutputStream outputFile = new FileOutputStream("Extracted File");


            int width = img.getWidth();
            int height = img.getHeight();

            int b;
            int p_index = 0;

            FileInputStream imageStego = new FileInputStream(args[0]);


            while ((b = imageStego.read()) != -1) {

                try {

                    // Checks green/blue channels
                    extractGreenBlue(img, p_index % width, p_index / width, 0);
                    p_index++;

                    extractGreenBlue(img, p_index % width, p_index / width, 0);
                    p_index++;

                    extractGreenBlue(img, p_index % width, p_index / width, 0);
                    p_index++;

                    extractGreenBlue(img, p_index % width, p_index / width, 0);
                    p_index++;

                    outputFile.write((byte) (Integer.parseInt(hiddenImg, 2)));
                    hiddenImg = "";

                } catch (ArrayIndexOutOfBoundsException e) {
                    break;
                }
            }

        }
    }
}


