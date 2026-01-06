package Objects;
import processing.core.*;

/**
 *
 * @author Helpful273
 */
public class Character extends CircleCollider {
    // DEFAULTS
    private final static int[] HITBOX_COLOUR = {255, 255, 255};
    
    /*
    Constructor for circle collider.
    @param app The parent applet.
    @param x The initial x position.
    @param y The initial y position.
    @param radius The initial radius.
    */
    public Character(PApplet app, int x, int y, int radius) {
        super(app, x, y, radius, HITBOX_COLOUR);
    }
}
