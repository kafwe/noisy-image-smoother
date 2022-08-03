public class MedianFilterSerial extends Filter {

    /**
     * Constructs a new MedianFilterSerial object with the specified window width.  
     * 
     * @param windowWidth the width of the window to use for the filter
     * @throws IllegalArgumentException if windowWidth is not odd 
     * or if windowWidth is less than 3
     * @see Filter#Filter(int)
     */
    public MedianFilterSerial(int windowWidth) {
        super(windowWidth);
    }

    


    
}