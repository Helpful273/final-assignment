package Utils;

/**
 *
 * @author Helpful273
 */
public class Vector2 {
    /*
    Rotates a vector in degrees counter-clockwise.
    @param x The x coordinate in the vector
    @param y The y coordinate in the vector
    @param rot The amount to rotate the vector.
    */
    public static int[] RotateVector(int x, int y, int rot) {
        int[] rotatedVector = new int[2];
        
        double rotInRad = Math.toRadians(rot);
        
        rotatedVector[0] = (int) Math.round(x * Math.cos(rotInRad) - y * Math.sin(rotInRad));
        rotatedVector[1] = (int) Math.round(x * Math.sin(rotInRad) + y * Math.cos(rotInRad));
        
        return rotatedVector;
    }
}
