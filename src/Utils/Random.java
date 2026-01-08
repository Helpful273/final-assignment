package Utils;

/**
 *
 * @author Helpful273
 */
public class Random {
    /*
    Gets a random number between two numbers.
    @param min The minimum value of the range.
    @param max The maximum value of the range.
    @return The random integer between ranges.
    */
    public static int InRange(int min, int max) {
        return (int) (Math.random() * ((max - min) + 1)) + min;
    }
}
