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

    public static void main(String[] args) {
        //String input = args[0];
        //String output = args[1];
        int windowWidth = Integer.parseInt(args[2]);

        try {
            MeanFilterSerial meanFilter = new MeanFilterSerial(windowWidth);
            //meanFilter.apply(input);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
        
    }
    
}