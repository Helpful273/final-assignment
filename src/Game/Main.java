package Game;
import processing.core.PApplet;
import Objects.*;
import Utils.*;

/**
 *
 * @author Helpful273
 */
public class Main extends PApplet {
    private BulletPool pool;
    private Actor dummy;
    
    public void settings() {
        size(800, 800);
    }
    
    public void setup() {
        pool = new BulletPool(this, 1);
        dummy = new Actor(this, "Assets/Characters/Character.PNG", 1, 400, 600, 3);
        dummy.addCollisionListener(pool.getActivePool());
        dummy.setRotationSpeed(1);
        
    }
    
    public void draw() {
        Bullet test = pool.spawnBullet(400, 50);
        test.setRotation(dummy.getRotation());
        test.setRotationSpeed(1);
        test.setRadius(10);
        test.setSpeed(1);
                
        
        background(200);
        pool.update();
        dummy.update();
        dummy.draw();
        dummy.drawHitbox();
        pool.draw();
    }
}
