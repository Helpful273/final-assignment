package Objects;
import processing.core.*;

/**
 *
 * @author Helpful273
 */
public class Bullet {
    public int x, y = -100;
    
    // bullet trajectory
    private int xDirection, yDirection = 0;
    private int xAcceleration, yAcceleration = 0;
    
    // bullet properties
    private int speed = 0;
    private int accelerationSpeed = 0;
    private int damage = 0;
    private int size = 0;
    
    // core
    private PApplet app;
    private PImage image;
    
    public Bullet() {
        
    }
}
