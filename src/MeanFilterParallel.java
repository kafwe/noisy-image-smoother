import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import javax.imageio.ImageIO;

public class MeanFilterParallel extends RecursiveAction {

    private BufferedImage source;
    private int start;
    private int length;
    private  BufferedImage destination;
    private int windowWidth;
    private static final int SEQUENTIAL_CUTOFF = 100;
    
    public MeanFilterParallel(BufferedImage source, int start, int length, BufferedImage destination, int windowWidth) {
        this.source = source;
        this.start = start;
        this.length = length;
        this.destination = destination;
        this.windowWidth = windowWidth;
    }

    public static boolean isValidWindowWidth(int width) {       
        if (width % 2 == 0 || width < 3) {
            throw new IllegalArgumentException(
                "Window width must be odd and greater than 2");
        }

        return true;

    }

    protected void computeDirectly() {
        int width = source.getWidth();
        int height = source.getHeight();
        int neighbouringPixels = (windowWidth - 1) / 2;

        // keep in bounds of image
        start = Math.max(start, neighbouringPixels);
        length = Math.min(length, width - neighbouringPixels - start);

        // iterate through each pixel in the image
        for (int x = start; x < start + length; x++) {
            for (int y = neighbouringPixels; y < height - neighbouringPixels; y++) {
                int red = 0;
                int green = 0;
                int blue = 0;

                // iterate through each pixel in the window
                for (int i = x - neighbouringPixels; i <= x + neighbouringPixels; i++) {
                    for (int j = y - neighbouringPixels; j <= y + neighbouringPixels; j++) {
                        int pixel = source.getRGB(i, j);
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
                destination.setRGB(x, y, filteredPixel);
            }
        }
    }  

    @Override
    protected void compute() {
        if (length <= SEQUENTIAL_CUTOFF) {
            computeDirectly();
            return;
        }

        int mid = length / 2;
        MeanFilterParallel left = new MeanFilterParallel(source, start, mid, destination, windowWidth);
        MeanFilterParallel right = new MeanFilterParallel(source, start + mid, length - mid, destination, windowWidth);
        left.fork();
        right.compute();
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

    public static BufferedImage smooth(BufferedImage image, int windowWidth) {
        int width = image.getWidth();
        BufferedImage filteredImage = new BufferedImage(width, image.getHeight(), image.getType());

        MeanFilterParallel task = new MeanFilterParallel(image, 0, width, filteredImage, windowWidth);
        ForkJoinPool pool = new ForkJoinPool();

        long startTime = System.currentTimeMillis();
        pool.invoke(task);
        long endTime = System.currentTimeMillis();

        System.out.println("Parallel Mean Filter took " + (endTime - startTime) + 
                " milliseconds.");

        return filteredImage;
    }
    
}