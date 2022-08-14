
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.io.IOException;

class FilterTests {
    @ParameterizedTest
    @ValueSource(strings = {"1", "2", "3", "4"})
    @DisplayName("Mean Filter Test") 
    void testMeanFilter(int imageNum) throws IOException {
        File serialFile = new File("images/MeanFilterSerial/galactic" 
        + imageNum + "-MeanFilterSerial.jpg");
        BufferedImage serialImage = ImageIO.read(serialFile);

        File parallelFile = new File("images/MeanFilterParallel/galactic" 
        + imageNum + "-MeanFilterParallel.jpg");
        BufferedImage parallelImage = ImageIO.read(parallelFile);

        int[] serialImagePixels = serialImage.getRGB(0, 0, serialImage.getWidth(), 
        serialImage.getHeight(), null, 0, serialImage.getWidth());

        int[] parallelImagePixels = parallelImage.getRGB(0, 0, 
        parallelImage.getWidth(), parallelImage.getHeight(), null, 0, parallelImage.getWidth());

        assertArrayEquals(serialImagePixels, parallelImagePixels);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4})
    @DisplayName("Median Filter Test")
    void testMedianFilter(int imageNum) throws IOException {
        File serialFile = new File("images/MedianFilterSerial/galactic" 
        + imageNum + "-MedianFilterSerial.jpg");
        BufferedImage serialImage = ImageIO.read(serialFile);

        File parallelFile = new File("images/MedianFilterParallel/galactic" 
        + imageNum + "-MedianFilterParallel.jpg");
        BufferedImage parallelImage = ImageIO.read(parallelFile);

        int[] serialImagePixels = serialImage.getRGB(0, 0, serialImage.getWidth(), 
        serialImage.getHeight(), null, 0, serialImage.getWidth());

        int[] parallelImagePixels = parallelImage.getRGB(0, 0, 
        parallelImage.getWidth(), parallelImage.getHeight(), null, 0, parallelImage.getWidth());

        assertArrayEquals(serialImagePixels, parallelImagePixels);
    }
}