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
    private Actor test;
    private int t = 0;
    private int mt = 3;
    
    public void settings() {
        size(800, 800);
    }
    
    public void setup() {
        pool = new BulletPool(this);
        test = new Actor(this, "Assets/Characters/Character.PNG", 400, 400, 10);
    }
    
    public void draw() {
        background(200);
        
        if (t >= mt) {
            t = 0;
            Bullet n = pool.getBullet();
            n.setRotation(90);
            n.setSpeed(1);
            n.moveTo(Random.InRange(1, 799), 1);
            n.setRadius(30);
        }
        
        t++;
        
        pool.update();
        
        test.draw();
        test.drawHitbox();
        pool.draw();
    }
}
