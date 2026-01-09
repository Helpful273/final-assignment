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
        
    }
    
    public void draw() {
        
        Bullet test = pool.spawnBullet(400, 50);
        test.setRotation(90);
        test.setRadius(10);
        test.setSpeed(3);
        
        background(200);
        pool.update();
        dummy.update();
        pool.draw();
        dummy.draw();
    }
}
