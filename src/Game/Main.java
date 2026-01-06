package Game;
import processing.core.PApplet;
import Objects.*;

/**
 *
 * @author Helpful273
 */
public class Main extends PApplet {
    private BulletPool pool;
    
    public void settings() {
        size(1000, 1000);
    }
    
    public void setup() {
        pool = new BulletPool(this);
        
        Bullet thisBullet = pool.GetBullet();
        thisBullet.x = 100;
        thisBullet.y = 500;
        thisBullet.SetRadius(5);
        thisBullet.SetSpeed(1);
        thisBullet.SetRotationSpeed(1);
    }
    
    public void draw() {
        background(200);
        
        pool.update();
        pool.draw();
    }
}
