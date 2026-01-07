package Objects;
import Utils.Vector2;
import processing.core.PApplet;

/**
 *
 * @author Helpful273
 */
public class Bullet extends CircleCollider {
    // CONSTANTS
    private final static int DEFAULT_POSITION = -100;
    private final static int DEFAULT_RADIUS = 3;
    /* 
    This will always represent "x" in a (x, 0) vector. With this in conjunction
    with 0 rotation the bullet will move toward the right of the screen at
    "x" pixels per frame at 60 frames per second.
    */
    private final static int DEFAULT_MOVE_INCREMENT = 4;
    
    // bullet trajectory
    private int rot = 0;
    
    // bullet properties
    private int speed = 0;
    private int rotSpeed = 0;
    private int damage = 0;
    
    // core
    private final BulletPool pool;
    
    /*
    Bullet constructor.
    @param app The parent applet.
    @param pool The BulletPool the bullet belongs to
    */
    public Bullet(PApplet app, BulletPool pool) {
        super(app, DEFAULT_POSITION, DEFAULT_POSITION, DEFAULT_RADIUS);
        this.pool = pool;
    }
    
    /*
    Updates the speed property of the bullet.
    @param speed The new speed of the bullet.
    */
    public void setSpeed(int speed) {
        this.speed = speed;
    }
    
    /*
    Updates the rotation property of the bullet.
    @param rot The new rotation of the bullet.
    */
    public void setRotation(int rot) {
        this.rot = rot;
    }
    
    /*
    Updates the rotation speed property of the bullet.
    @param speed The new rotation speed of the bullet.
    */
    public void setRotationSpeed(int rotSpeed) {
        this.rotSpeed = rotSpeed;
    }
    
    /*
    Updates the set damage property of the bullet.
    @param speed The new damage of the bullet.
    */
    public void setDamage(int damage) {
        this.damage = damage;
    }
    
    /*
    Updates the bullet.
    */
    public void update() {
        int[] vector = Vector2.RotateVector(DEFAULT_MOVE_INCREMENT * speed, 0, rot);
        
        x += vector[0];
        y += vector[1];
        
        rot += rotSpeed;
    }
    
    /*
    Recalls this object to its inactive pool. See BulletPool.java
    */
    public void kill() {
        pool.recall(this);
    }
}