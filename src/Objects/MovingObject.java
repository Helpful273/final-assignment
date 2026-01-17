package Objects;
import processing.core.*;
import Utils.*;

/**
 *
 * @author Helpful273
 */
public class MovingObject {
    // CONSTANTS
    /* 
    This will always represent "x" in a (x, 0) vector. With this in conjunction
    with 0 rotation the bullet will move toward the right of the screen at
    "x" pixels per frame at 60 frames per second.
    */
    public final static int[] DEFAULT_VECTOR = {5, 0};
    private final static int DEFAULT_COLOUR = 100;
    
    // position
    public int x, y;
    private int startX, startY;
    private int targetX, targetY;
    private boolean moveFlag = false;
    private double time = 0;
    private double duration = 0;
    
    // collider properties
    private int radius;
    private int[] colourRGB = {DEFAULT_COLOUR, DEFAULT_COLOUR, DEFAULT_COLOUR};
    private int speed = 1;
    private int rot = 0;
    private int rotSpeed = 0;
    
    // core
    private final PApplet app;
    
    /*
    Constructor for circle collider.
    @param app The parent applet.
    @param x The initial x position.
    @param y The initial y position.
    @param radius The initial radius.
    */
    public MovingObject(PApplet app, int x, int y, int radius) {
        this.app = app;
        this.x = x;
        this.y = y;
        this.radius = radius;
    }
    
    /*
    Moves the moving object to a new position.
    @param x The new x position.
    @param y The new y position.
    */
    public void moveToInstant(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    /*
    Moves the moving object over a specified amount of time.
    @param x End x position.
    @param y End y position.
    @param time The amount of time it should take to finish the move.
    */
    public void moveTo(int x, int y, double time) {
        // set all the necessray fields
        duration = 0;
        this.startX = this.x;
        this.startY = this.y;
        this.targetX = x;
        this.targetY = y;
        // make sure that the time is converted to ticks
        this.time = time * 60;
        // enable move flag.
        moveFlag = true;
    }
    
    /*
    Updates the radius property of the moving object.
    @param radius The new radius of the moving object.
    */
    public void setRadius(int radius) {
        this.radius = radius;
    }
    
    /*
    Updates the colour property of the moving object.
    @param colourRGB The colour in an RGB format.
    */
    public void setColour(int[] colourRGB) {
        this.colourRGB = colourRGB;
    }
    
    /*
    Updates the speed property of the moving object.
    @param speed The new speed of the moving object.
    */
    public void setSpeed(int speed) {
        this.speed = speed;
    }
    
    /*
    Updates the rotation property of the moving object.
    @param rot The new rotation of the moving object.
    */
    public void setRotation(int rot) {
        this.rot = rot;
    }
    
    /*
    Updates the rotation speed property of the moving object.
    @param speed The new rotation speed of the moving object.
    */
    public void setRotationSpeed(int rotSpeed) {
        this.rotSpeed = rotSpeed;
    }
    
    /*
    Updates the rotation property of the moving object based on a target position.
    */
    public void setRotationFromTarget(int x, int y) {
        // saves two positions as a vector
        PVector v1 = new PVector(DEFAULT_VECTOR[0], DEFAULT_VECTOR[1]);
        PVector v2 = new PVector(x - this.x, y - this.y);
        // get dot product of two vectors
        float dotProduct = PVector.dot(v1, v2);
        
        // use arccos get the theta between the two vectors and convert to degrees
        this.setRotation((int) Math.toDegrees(Math.acos(dotProduct / (v1.mag() * v2.mag()))));
    }
    
    /*
    Gets the speed property
    @return The speed of the moving object
    */
    public int getSpeed() {
        return speed;
    }
    
    /*
    Gets the rotation property
    @return The rotation of the moving object
    */
    public int getRotation() {
        return rot;
    }
    
    /*
    Gets the rotation speed property
    @return The rotation speed of the moving object
    */
    public int getRotationSpeed() {
        return rotSpeed;
    }
    
    /*
    Returns the radius property of the moving object.
    @return The radius of the moving object.
    */
    public int getRadius() {
        return radius;
    }
    
    /*
    Returns the colour property of the moving object.
    @return The clouur in RGB format.
    */
    public int[] getColour() {
        return colourRGB;
    }
    
    /*
    Updates the moving object
    */
    public void update() {
        // check if there is a lerp playing.
        if (moveFlag) {
            // pivot the object with linear interpolation (lerp)
            int[] lerpedPosition = Vector2.Lerp(startX, startY, targetX, targetY, duration/time);
            this.x = lerpedPosition[0];
            this.y = lerpedPosition[1];
            
            // check if the lerp is finished if it is disable moveflag
            if (duration >= time) {
                duration = 0;
                moveFlag = false;
            }
            
            // increment lerp var
            duration++;
        }
        
        // rotate
        rot += rotSpeed;
    }
    
    /*
    Draws the moving object.
    */
    public void draw() {
        app.fill(colourRGB[0], colourRGB[1], colourRGB[2]);
        app.circle(x, y, radius);
    }
    
    /*
    Checks if two moving objects are colliding.
    @return If the other moving object is in contact with this moving object.
    */
    public boolean isColliding(MovingObject other) {
        return PApplet.dist(x, y, other.x, other.y) < radius/2 + other.getRadius()/2;
    }
    
    /*
    Checks if the moving object is in screen bounds.
    @return If the moving object is within screen bounds.
    */
    public boolean inBounds() {
        return x > 0 && x < app.width && y > 0 && y < app.height;
    }
}
