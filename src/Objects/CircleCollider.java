package Objects;
import processing.core.*;
import Utils.*;

/**
 *
 * @author Helpful273
 */
public class CircleCollider {
    // CONSTANTS
    private final static int DEFAULT_COLOUR = 100;
    
    // position
    public int x, y;
    
    // collider properties
    private int radius;
    private int[] colourRGB = {DEFAULT_COLOUR, DEFAULT_COLOUR, DEFAULT_COLOUR};
    
    // core
    private PApplet app;
    
    /*
    Constructor for circle collider.
    @param app The parent applet.
    @param x The initial x position.
    @param y The initial y position.
    @param radius The initial radius.
    */
    public CircleCollider(PApplet app, int x, int y, int radius) {
        this.app = app;
        this.x = x;
        this.y = y;
        this.radius = radius;
    }
    
    /*
    Moves the collider to a new position.
    @param x The new x position.
    @param y The new y position.
    */
    public void moveTo(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    /*
    Updates the radius property of the collider.
    @param radius The new radius of the collider.
    */
    public void setRadius(int radius) {
        this.radius = radius;
    }
    
    /*
    Updates the colour property of the collider.
    @param colourRGB The colour in an RGB format.
    */
    public void setColour(int[] colourRGB) {
        this.colourRGB = colourRGB;
    }
    
    /*
    Returns the radius property of the collider.
    @return The radius.
    */
    public int getRadius() {
        return radius;
    }
    
    /*
    Draws the collider.
    */
    public void draw() {
        app.fill(colourRGB[0], colourRGB[1], colourRGB[2]);
        app.circle(x, y, radius);
    }
    
    /*
    Checks if two colliders are colliding.
    @return If the other collider is in contact with this collider.
    */
    public boolean isColliding(CircleCollider other) {
        return Vector2.GetDistance(x, y, other.x, other.y) < radius + other.getRadius();
    }
    
    /*
    Checks if the collider is in screen bounds.
    @return If the collider is within screen bounds.
    */
    public boolean inBounds() {
        return x > 0 && x < app.displayWidth && y > 0 && y < app.displayHeight;
    }
}
