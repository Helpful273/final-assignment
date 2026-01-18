package Objects;
import Utils.Vector2;
import processing.core.PApplet;

/**
 *
 * @author Helpful273
 */
public class Bullet extends MovingObject {
    // CONSTANTS
    private final static int DEFAULT_POSITION = -100;
    public final static int DEFAULT_RADIUS = 10;
    
    // bullet properties
    private int damage = 1;
    
    // core
    public boolean toKill;
    
    /*
    Bullet constructor.
    @param app The parent applet.
    */
    public Bullet(PApplet app) {
        super(app, DEFAULT_POSITION, DEFAULT_POSITION, DEFAULT_RADIUS);
    }
    
    /*
    Updates the set damage property of the bullet.
    @param speed The new damage of the bullet.
    */
    public void setDamage(int damage) {
        this.damage = damage;
    }
    
    /*
    Gets the damage property of the bullet.
    @return The damage property.
    */
    public int getDamage() {
        return damage;
    }
    
    /*
    Updates the bullet.
    */
    @Override
    public void update() {
        // rotates the directional based on  rotation
        int[] vector = Vector2.RotateVector((int) (DEFAULT_VECTOR[0] * super.getSpeed()), DEFAULT_VECTOR[1], super.getRotation());
        
        // updates pos
        x += vector[0];
        y += vector[1];
        
        super.update();
    }
}