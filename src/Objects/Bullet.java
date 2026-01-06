package Objects;
import Utils.BasicVector2;
import processing.core.PApplet;

/**
 *
 * @author Helpful273
 */
public class Bullet {
    public int x, y = -100;
    
    // bullet trajectory
    private int rot = 0;
    
    // bullet properties
    private int speed = 0;
    private int rotSpeed = 0;
    private int damage = 0;
    private int radius = 0;
    
    // core
    private final PApplet app;
    private final BulletPool pool;
    
    public Bullet(PApplet app, BulletPool pool) {
        this.app = app;
        this.pool = pool;
    }
    
    public void SetSpeed(int newSpeed) {
        speed = newSpeed;
    }
    
    public void SetRotationSpeed(int newRotSpeed) {
        rotSpeed = newRotSpeed;
    }
    
    public void SetDamage(int newDamage) {
        damage = newDamage;
    }
    
    public void SetRadius(int newRadius) {
        radius = newRadius;
    }
    
    public void update() {
        int[] vector = BasicVector2.RotateVector(4, 0, rot);
        
        x += vector[0];
        y += vector[1];
        
        rot += rotSpeed;
    }
    
    public void draw() {
        app.fill(0);
        app.circle(x, y, radius);
    }
    
    /*
    Recalls this object to its inactive pool. See BulletPool.java
    */
    public void Kill() {
        pool.Recall(this);
    }
}