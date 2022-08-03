import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * A sequential mean filter implementation.
 * 
 * @author Jordy Kafwe
 */
public class MeanFilterSerial {

    private int windowWidth;

    /**
     * Constructs a new MeanFilterSerial object with the specified window width.
     * 
     * @param windowWidth the width of the window to use for the mean filter
     * @throws IllegalArgumentException if windowWidth is not odd
     * @throws IllegalArgumentException if windowWidth is less than 3
     */
    public MeanFilterSerial(int windowWidth) {
        setWindowWidth(windowWidth);
    }

    /**
     * Returns the window width of the mean filter.
     * 
     * @return the int representing the window width
     */
    public int getWindowWidth() {
        return windowWidth;
    }

    /**
     * Sets the window width of the mean filter.
     * 
     * @param windowWidth the width of the window to use for the mean filter
     * @throws IllegalArgumentException if windowWidth is not odd
     * @throws IllegalArgumentException if windowWidth is less than 3
     */
    public void setWindowWidth(int windowWidth) {
        if (windowWidth % 2 == 0) {
            throw new IllegalArgumentException("Window width must be odd");
        }

        if (windowWidth < 3) {
            throw new IllegalArgumentException("Window width must be at least 3");
        }

        this.windowWidth = windowWidth;
    }

    /**
     * @param values
     * @return
     */
    public int computeMean(int[] values) {
        int sum = 0;

        for (int value : values) {
            sum += value;
        }

        return sum/values.length;
    }

    public BufferedImage apply(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage filteredImage = new BufferedImage(width, height, image.getType());

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int[] values = new int[windowWidth * windowWidth];

   
                int mean = computeMean(values);
                filteredImage.setRGB(x, y, mean);
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
            BufferedImage filteredImage = meanFilter.apply(inputImage);
            ImageIO.write(filteredImage, "jpg", outputFile);

        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } catch (IOException e) {
            System.out.println("File could not be opened.");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }        
    }
    
}