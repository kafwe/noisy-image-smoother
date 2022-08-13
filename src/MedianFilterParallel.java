import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import javax.imageio.ImageIO;

/**
 * A parallel median filter implementation to smooth 2D RGB images.
 * 
 * @author Jordy Kafwe
 */
public class MedianFilterParallel extends RecursiveAction {

    private BufferedImage source;
    private int start;
    private int length;
    private  BufferedImage destination;
    private int windowWidth;
    private static final int SEQUENTIAL_CUTOFF = 100; // cutoff for sequential processing
    
    /**
     * Constructs a new MedianFilterParallel object with the specified window width.
     * 
     * @param source the source image to apply the filter to
     * @param start the start index of the region to process
     * @param length the length of the region to process
     * @param destination the destination image to write the results to
     * @param windowWidth the width of the window to use for the filter
     */
    public MedianFilterParallel(BufferedImage source, int start, int length, BufferedImage destination, int windowWidth) {
        this.source = source;
        this.start = start;
        this.length = length;
        this.destination = destination;
        this.windowWidth = windowWidth;
    }

    /**
     * Checks that the specified window width is odd and greater than 2.
     * 
     * @param width the width of the window to check
     * @return true if the window width is valid, and throws an exception otherwise
     * @throws IllegalArgumentException if the window width is not odd or 
     * if it is less than 3
     */
    private static boolean isValidWindowWidth(int width) {       
        if (width % 2 == 0 || width < 3) {
            throw new IllegalArgumentException(
                "Window width must be odd and greater than 2");
        }

        return true;

    }

    /**
     * Computes the median of the neighbouring pixels in the specified window.
     * 
     * @param values the values of the neighbouring pixels
     * @return the int representing the median of the neighbouring pixels
     */
    private int computeMedian(int[] values) {
        Arrays.sort(values);
        return values[values.length / 2];
    }

    /**
     * Directly applies the median filter to a region of the source image.
     * Writes the results to the destination image.
     */
    protected void computeDirectly() {
        int width = source.getWidth();
        int height = source.getHeight();
        int neighbouringPixels = (windowWidth - 1) / 2;
        int windowSize = windowWidth * windowWidth;
        int[] redValues = new int[windowSize];
        int[] greenValues = new int[windowSize];
        int[] blueValues = new int[windowSize];

        // keep in bounds of image
        start = Math.max(start, neighbouringPixels);
        length = Math.min(length, width - neighbouringPixels - start);

        // iterate through each pixel in the image
        for (int x = start; x < start + length; x++) {
            for (int y = neighbouringPixels; y < height - neighbouringPixels; y++) {            
                int index = 0;

                // iterate through each pixel in the window
                for (int i = x -  neighbouringPixels; i <= x + neighbouringPixels; i++) {
                    for (int j = y - neighbouringPixels; j <= y + neighbouringPixels; j++) {
                        int pixel = source.getRGB(i, j);
                        redValues[index] = pixel >> 16 & 0xFF;
                        greenValues[index] = pixel >> 8 & 0xFF;
                        blueValues[index] = pixel & 0xFF;
                        index++;
                    }
                }

                int red = computeMedian(redValues);
                int green = computeMedian(greenValues);
                int blue = computeMedian(blueValues);
                // replace the pixel with the median of the neighbouring pixels
                int filteredPixel = red << 16 | green << 8 | blue;
                destination.setRGB(x, y, filteredPixel);
            }
        }
    }  

    /**
     * The main compute method for the MedianFilterParallel class.
     * 
     * Either performs the smoothing directly or splits it into two smaller tasks
     * and then executes them in parallel. 
     * The length variable represents the width (of the region of the image 
     * calling the method) and helps determine whether the work is small 
     * enough to be done directly or not. 
     */
    @Override
    protected void compute() {
        if (length <= SEQUENTIAL_CUTOFF) {
            computeDirectly();
            return;
        }

        // the midpoint of the region of the image calling the method
        int mid = length / 2;
        // split the region into two smaller regions
        MedianFilterParallel left = new MedianFilterParallel(source, start, mid, destination, windowWidth);
        MedianFilterParallel right = new MedianFilterParallel(source, start + mid, length - mid, destination, windowWidth);
        left.fork();
        right.compute();
        // wait for the left task to finish
        left.join();
    }

    public static void main(String[] args) {
        try {
            File inputFile = new File(args[0]);
            File outputFile = new File(args[1]);
            int windowWidth = Integer.parseInt(args[2]);
            isValidWindowWidth(windowWidth);
            BufferedImage inputImage = ImageIO.read(inputFile);
            BufferedImage filteredImage = smooth(inputImage, windowWidth);
            ImageIO.write(filteredImage, "jpeg", outputFile);

        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } catch (IOException e) {
            System.out.println("File could not be opened");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }        
    }

    /**
     * Smooths the specified image using a median filter with the specified window width.
     * 
     * @param image the image to smooth
     * @param windowWidth the width of the window to use for the filter
     * @return the smoothed image
     */
    public static BufferedImage smooth(BufferedImage image, int windowWidth) {
        int width = image.getWidth();
        BufferedImage filteredImage = new BufferedImage(width, image.getHeight(), image.getType());
        MedianFilterParallel task = new MedianFilterParallel(image, 0, width, 
        filteredImage, windowWidth);
        ForkJoinPool pool = new ForkJoinPool();

        // time the execution of the task
        long startTime = System.currentTimeMillis();
        pool.invoke(task);
        long endTime = System.currentTimeMillis();

        System.out.println("Parallel Mean Filter took " + (endTime - startTime) + 
                " milliseconds.");

        return filteredImage;
    }
    
}