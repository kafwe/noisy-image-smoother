import java.awt.image.BufferedImage;
import java.io.File;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import javax.imageio.ImageIO;

public class MeanFilterParallel extends RecursiveAction {

    private int[] source;
    private int start;
    private int length;
    private int[] destination;
    private int windowWidth;
    
    public MeanFilterParallel(int[] source, int start, int length, int[] destination, int windowWidth) {
        this.source = source;
        this.start = start;
        this.length = length;
        this.destination = destination;
        this.windowWidth = windowWidth;
    }
    
    public void setWindowWidth(int windowWidth) {
        if (windowWidth % 2 == 0 || windowWidth < 3) {
            throw new IllegalArgumentException(
                "Window width must be odd and greater than 2");
        }

        this.windowWidth = windowWidth;
    }

    public int getWindowWidth() {
        return windowWidth;
    }

    protected void computeDirectly() {
        int neighbouringPixels = (windowWidth - 1) / 2;
        for (int i = start; i < start + length; i++) {
            // Calculate average.
            int redValues = 0;
            int greenValues = 0; 
            int blueValues = 0;

            for (int j = -neighbouringPixels; j <= neighbouringPixels; j++) {
                int index = Math.min(Math.max(j + i, 0), source.length - 1);
                int pixel = source[index];
                redValues += pixel >> 16 & 0xFF;
                greenValues += pixel >> 8 & 0xFF;
                blueValues += pixel & 0xFF;
            }

            
            int red = redValues / (windowWidth * windowWidth);
            int green = greenValues / (windowWidth * windowWidth);
            int blue = blueValues / (windowWidth * windowWidth);
            // Re-assemble destination pixel.
            int dpixel = red << 16 | green << 8 | blue;
            destination[i] = dpixel;
        }
    }  

    @Override
    protected void compute() {
        
    }

    public static void main(String[] args) {
        
    }

    public static BufferedImage smooth(BufferedImage image) {

    }
    
}