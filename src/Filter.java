import java.awt.image.BufferedImage;

 /**
  * A filter implementation using a sliding window.
  * 
  * @author Jordy Kafwe
  */
public abstract class Filter {
    private int windowWidth;

    /**
     * Constructs a new Filter object with the specified window width.
     * 
     * @param windowWidth the width of the window to use for the filter
     * @throws IllegalArgumentException if windowWidth is not odd or if 
     * windowWidth is less than 3
     */
    public Filter(int windowWidth) {
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
     * Applies the filter to the specified image.
     * 
     * @param image the image to apply the filter to
     * @return the filtered image
     */
    public abstract BufferedImage apply(BufferedImage image);

}
