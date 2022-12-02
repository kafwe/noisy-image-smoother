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
    private static int WINDOW_WIDTH; // the width of the window to use for the filter
    private static int SEQUENTIAL_CUTOFF = 200; // cutoff for sequential processing
    
    /**
     * Constructs a new MedianFilterParallel object with the specified window width.
     * 
     * @param source the source image to apply the filter to
     * @param start the start index of the region to process
     * @param length the length (width) of the region to process
     * @param destination the destination image to write the results to
     */
    public MedianFilterParallel(BufferedImage source, int start, int length, BufferedImage destination) {
        this.source = source;
        this.start = start;
        this.length = length;
        this.destination = destination;
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
     * Directly applies the median filter to a region of the source image.
     * Writes the results to the destination image.
     */
    protected void applyFilter() {
        int width = source.getWidth();
        int height = source.getHeight();
        int neighbouringPixels = (WINDOW_WIDTH - 1) / 2;
        int windowSize = WINDOW_WIDTH * WINDOW_WIDTH;
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

                // determine the median value of the window
                Arrays.sort(redValues);
                Arrays.sort(greenValues);
                Arrays.sort(blueValues);
                int red = redValues[windowSize / 2];
                int green = greenValues[windowSize / 2];
                int blue = blueValues[windowSize / 2];

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
            applyFilter();
            return;
        }

        // the midpoint of the region of the image calling the method
        int mid = length / 2;
        // split the region into two smaller regions
        MedianFilterParallel left = new MedianFilterParallel(source, start, mid, destination);
        MedianFilterParallel right = new MedianFilterParallel(source, start + mid, 
        length - mid, destination);
        left.fork();
        right.compute();
        // wait for the left task to finish
        left.join();
    }

    public static void main(String[] args) {
        boolean testingSequentialCutoff = (args.length == 4);

        if (testingSequentialCutoff) {
            SEQUENTIAL_CUTOFF = Integer.parseInt(args[3]);
        } 

        try {
            File inputFile = new File(args[0]);
            File outputFile = new File(args[1]);
            WINDOW_WIDTH = Integer.parseInt(args[2]);
            isValidWindowWidth(WINDOW_WIDTH);
            BufferedImage inputImage = ImageIO.read(inputFile);
            BufferedImage filteredImage = smooth(inputImage);
            ImageIO.write(filteredImage, "jpeg", outputFile);

        } catch (FileNotFoundException e) {
            System.out.println("File not found or could not be read");
        } catch (IOException e) {
            System.out.println("File could not be written");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Missing arguments\n" +
                "Usage: java MedianFilterParallel <input file> <output file> <window width>");
        }
    }

    /**
     * Smooths the specified image using a median filter with the specified window width.
     * 
     * @param image the image to smooth
     * @return the smoothed image
     */
    public static BufferedImage smooth(BufferedImage image) {
        int width = image.getWidth();
        BufferedImage filteredImage = new BufferedImage(width, image.getHeight(), image.getType());
        MedianFilterParallel task = new MedianFilterParallel(image, 0, width, filteredImage);
        ForkJoinPool pool = new ForkJoinPool();

        // time the execution of the task
        long startTime = System.currentTimeMillis();
        pool.invoke(task);
        long endTime = System.currentTimeMillis();

        System.out.println(endTime - startTime);

        return filteredImage;
    }

}