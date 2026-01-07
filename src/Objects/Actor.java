package Objects;
import processing.core.*;

/**
 *
 * @author Helpful273
 */
public class Actor extends CircleCollider {
    // CONSTANTS
    private final static int[] HITBOX_COLOUR = {255, 255, 255};
    
    // character properties
    private int maxHealth;
    private int health;
    
    // core
    private PApplet app;
    private PImage image;
    
    /*
    Constructor for circle collider.
    @param app The parent applet.
    @param x The initial x position.
    @param y The initial y position.
    @param radius The initial radius.
    */
    public Actor(PApplet app, String imagePath, int x, int y, int radius) {
        super(app, x, y, radius);
        super.setColour(HITBOX_COLOUR);
        this.app = app;
        this.image = app.loadImage(imagePath);
    }
    
    /*
    Draws the character.
    */
    @Override
    public void draw() {
        app.image(image, x - image.width/2, y - image.height/2);
    }
    
    /*
    Draws the character's hitbox.
    */
    public void drawHitbox() {
        super.draw();
    }
}
