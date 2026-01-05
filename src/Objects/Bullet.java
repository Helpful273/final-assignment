package Objects;
import processing.core.PApplet;

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
    private int radius = 0;
    
    // core
    private final PApplet app;
    private final BulletPool pool;
    
    public Bullet(PApplet app, BulletPool pool) {
        this.app = app;
        this.pool = pool;
    }
    
    public void update() {
        
    }
    
    public void draw() {
        app.circle(x, y, radius);
    }
    
    /*
    Recalls this object to its inactive pool. See BulletPool.java
    */
    public void Kill() {
        pool.Recall(this);
    }
}