import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * A sequential mean filter implementation to smooth 2D RGB images.
 * 
 * @author Jordy Kafwe
 */
public class MeanFilterSerial {

    private int windowWidth;

    /**
     * Constructs a new MeanFilterSerial object with the specified window width.
     * 
     * @param windowWidth the width of the window to use for the filter
     * @throws IllegalArgumentException if windowWidth is not odd or if 
     * windowWidth is less than 3
     */
    public MeanFilterSerial(int windowWidth) {
        setWindowWidth(windowWidth);
    }

    /**
     * Returns the window width of the filter.
     * 
     * @return the int representing the window width
     */
    public int getWindowWidth() {
        return windowWidth;
    }

    /**
     * Sets the window width of the filter.
     * 
     * @param windowWidth the width of the window to use for the filter. 
     * @throws IllegalArgumentException if windowWidth is not odd or if 
     * windowWidth is less than 3
     */
    public void setWindowWidth(int windowWidth) {
        if (windowWidth % 2 == 0 || windowWidth < 3) {
            throw new IllegalArgumentException(
                "Window width must be odd and greater than 2");
        }

        this.windowWidth = windowWidth;
    }

    /**
     * Applies the mean filter to the specified image.
     * 
     * @param image the image to apply the filter to
     * @return the filtered image
     */
    public BufferedImage apply(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        int neighbouringPixels = (windowWidth - 1) / 2;
        BufferedImage filteredImage = new BufferedImage(width, height, image.getType());
        

        // iterate through each pixel in the image
        for (int x = neighbouringPixels ; x < width - neighbouringPixels; x++) {
            for (int y = neighbouringPixels; y < height - neighbouringPixels; y++) {
                int red = 0;
                int green = 0;
                int blue = 0;

                // iterate through each pixel in the window
                for (int i = x - neighbouringPixels; i <= x + neighbouringPixels; i++) {
                    for (int j = y - neighbouringPixels; j <= y + neighbouringPixels; j++) {
                        int pixel = image.getRGB(i, j);
                        red += pixel >> 16 & 0xFF;
                        green += pixel >> 8 & 0xFF;
                        blue += pixel & 0xFF;
                    }
                }

                // compute the mean of the neighbouring pixels
                int windowSize = windowWidth * windowWidth;
                red /= windowSize;
                green /= windowSize;
                blue /= windowSize;

                int filteredPixel = red << 16 | green << 8 | blue;
                filteredImage.setRGB(x, y, filteredPixel);
            }
        }
        return filteredImage;
    }

    public static void main(String[] args) {
        try {
            File inputFile = new File(args[0]);
            File outputFile = new File(args[1]);
            int windowWidth = Integer.parseInt(args[2]);

            MeanFilterSerial meanFilter = new MeanFilterSerial(windowWidth);
            BufferedImage inputImage = ImageIO.read(inputFile);

            // time the execution of the filter
            long startTime = System.currentTimeMillis();
            BufferedImage filteredImage = meanFilter.apply(inputImage);
            long endTime = System.currentTimeMillis();

            ImageIO.write(filteredImage, "jpeg", outputFile);
            System.out.println(endTime - startTime);

        } catch (FileNotFoundException e) {
            System.out.println("File not found or could not be read");
        } catch (IOException e) {
            System.out.println("File could not be written");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Missing Arguments\n" + 
            "Usage: java MeanFilterSerial <inputImageName> <outputImageName> <windowWidth>");
        }       
    }
    
}