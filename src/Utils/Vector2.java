package Utils;

/**
 *
 * @author Helpful273
 */
public class Vector2 {
    /*
    Returns a rotated vector in degrees counter-clockwise.
    @param x The x coordinate in the vector.
    @param y The y coordinate in the vector.
    @param rot The amount to rotate the vector.
    */
    public static int[] RotateVector(int x, int y, int rot) {
        int[] rotatedVector = new int[2];
        
        // convert deg to rad
        double rotInRad = Math.toRadians(rot);
        
        // get new vector positions
        rotatedVector[0] = (int) Math.round(x * Math.cos(rotInRad) - y * Math.sin(rotInRad));
        rotatedVector[1] = (int) Math.round(x * Math.sin(rotInRad) + y * Math.cos(rotInRad));
        
        return rotatedVector;
    }
    
    /*
    Gets the distance between two vectors.
    @param x The x coordinate of the first vector.
    @param y The y coordinate of the second vector.
    @param x2 The x coordinate of the first vector.
    @param y2 The y coordinate of the second vector.
    */
    public static int GetDistance(int x, int y, int x2, int y2) {
        return (int) Math.round(Math.sqrt((x2 - x)^2 + (y2- y)^2));
    }
}
