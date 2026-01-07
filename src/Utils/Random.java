package Utils;

/**
 *
 * @author Helpful273
 */
public class Random {
    public static int InRange(int min, int max) {
        return (int) (Math.random() * ((max - min) + 1)) + min;
    }
}
