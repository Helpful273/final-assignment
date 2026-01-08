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
        test = new Actor(this, "Assets/Characters/Character.PNG", 1, 400, 400, 10);
        test.setRotationSpeed(5);
    }
    
    public void draw() {
        background(200);
        
        if (t >= mt) {
            
            t = 0;
            
            for (int i = 0; i < 4; i++) {
                Bullet n = pool.getBullet(test.x, test.y);
                n.setRotation(test.getRotation() + i * 90);
                n.setSpeed(4);
                n.setRadius(30);
                n.setRotationSpeed(3);
                int[] colour = {Random.InRange(0, 255), Random.InRange(0, 255), Random.InRange(0, 255)};
                
                n.setColour(colour);
            }
        }
        
        t++;
        
        pool.update();
        
        test.update();
        test.draw();
        test.drawHitbox();
        pool.draw();
    }
}
