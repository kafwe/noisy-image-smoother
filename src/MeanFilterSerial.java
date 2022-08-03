import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.awt.Color;

/**
 * A sequential mean filter implementation to smooth 2D RGB images.
 * 
 * @author Jordy Kafwe
 */
public class MeanFilterSerial extends Filter {

    /**
     * Constructs a new MeanFilterSerial object with the specified window width.
     * 
     * @param windowWidth the width of the window to use for the filter
     * @throws IllegalArgumentException if windowWidth is not odd or if 
     * windowWidth is less than 3
     * @see Filter#Filter(int)
     */
    public MeanFilterSerial(int windowWidth) {
        super(windowWidth);
    }

    /**
     * Computes the mean of the neighbouring pixels in the specified window.
     * 
     * @param values the values of the neighbouring pixels
     * @return the int representing the mean of the neighbouring pixels
     */
    private int computeMean(int[] values) {
        int sum = 0;

        for (int value : values) {
            sum += value;
        }

        return sum/values.length;
    }

    /**
     * Applies the mean filter to the specified image.
     * 
     * @param image the image to apply the filter to
     * @return the filtered image
     * @see Filter#apply(BufferedImage)
     */
    @Override
    public BufferedImage apply(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage filteredImage = new BufferedImage(width, height, image.getType());

        // iterate through each pixel in the image
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int[] redValues = new int[getWindowWidth() * getWindowWidth()];
                int[] greenValues = new int[getWindowWidth() * getWindowWidth()];
                int[] blueValues = new int[getWindowWidth() * getWindowWidth()];
                
                int index = 0;

                // iterate through each pixel in the window
                for (int i = x - (getWindowWidth() / 2); i <= x + (getWindowWidth() / 2); i++) {
                    for (int j = y - (getWindowWidth() / 2); j <= y + (getWindowWidth() / 2); j++) {
                        boolean inBounds = i >= 0 && i < width && j >= 0 && j < height;
                        if (inBounds) {
                            int rgb = image.getRGB(i, j);
                            Color pixel = new Color(rgb);
                            redValues[index] = pixel.getRed();
                            greenValues[index] = pixel.getGreen();
                            blueValues[index] = pixel.getBlue();
                            index++;
                        }
                    }
                }

                int red = computeMean(redValues);
                int green = computeMean(greenValues);
                int blue = computeMean(blueValues);
                int filteredPixel = new Color(red, green, blue).getRGB();
                
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
            BufferedImage filteredImage = meanFilter.apply(inputImage);
            ImageIO.write(filteredImage, "jpeg", outputFile);

        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } catch (IOException e) {
            System.out.println("File could not be opened");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }        
    }
    
}