package Objects;
import processing.core.*;

/**
 *
 * @author Helpful273
 */
public class CircleCollider {
    // DEFAULTS
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
    Constructor for circle collider.
    @param app The parent applet.
    @param x The initial x position.
    @param y The initial y position.
    @param radius The initial radius.
    @param colourRGB The colour the collider will use when drawn in RGB format.
    */
    public CircleCollider(PApplet app, int x, int y, int radius, int[] colourRGB) {
        this(app, x, y, radius);
        this.colourRGB = colourRGB;
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
    @param The new radius of the collider
    */
    public void setRadius(int radius) {
        this.radius = radius;
    }
    
    /*
    Draws the collider.
    */
    public void draw() {
        app.fill(colourRGB[0], colourRGB[1], colourRGB[2]);
        app.circle(x, y, radius);
    }
    
    
    public boolean isColliding(CircleCollider other) {
        return true;
    } 
}
