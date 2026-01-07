package Objects;
import processing.core.PApplet;
import java.util.ArrayList;
import java.util.NoSuchElementException;

/**
 *
 * @author Helpful273
 */
public class BulletPool {
    // CONSTANTS
    private final static int DEFAULT_INSTANCE_POSITION = -100;
    private final static int DEFAULT_INIT_POOL_SIZE = 100;
    
    // core
    private final PApplet app;
    private final ArrayList<Bullet> _activeBullets = new ArrayList();
    private final ArrayList<Bullet> _inactiveBullets = new ArrayList();
    
    /*
    Creates a new pool of bullets.
    @param app The parent applet.
    @param initialAmount The initial amount of bullets to be loaded into the
    cache.
    */
    public BulletPool(PApplet app, int initialAmount) {
        this.app = app;
        
        for (int i = 0; i < initialAmount; i++) {
            Bullet newBullet = new Bullet(app, this);
            _inactiveBullets.add(newBullet);
        }
    }
    
    /*
    Creates a new pool of bullets.
    @param app The parent applet.
    */
    public BulletPool(PApplet app) {
        this(app, DEFAULT_INIT_POOL_SIZE);
    }
    
    /*
    Updates all bullets in the active bullet pool.
    */
    public void update() {
        for (Bullet bullet: _activeBullets) {
            bullet.update();
            
            if (!bullet.inBounds()) {
                bullet.kill();
            }
        }
    }
    
    /*
    Draws all bullets in the active bullet pool.
    */
    public void draw() {
        for (Bullet bullet: _activeBullets) {
            bullet.draw();
        }
    }
    
    /*
    Recalls all bullets in the pool, setting them to an inactive state. 
    */
    public void recall() {
        for (Bullet bullet: _activeBullets) {
            recall(bullet);
        }
    }
    
    /*
    Recalls a bullet in the pool, setting them to an inactive state.
    @param bullet The bullet object to recall.
    */
    public void recall(Bullet bullet) {
        bullet.moveTo(DEFAULT_INSTANCE_POSITION, DEFAULT_INSTANCE_POSITION);
        _inactiveBullets.add(bullet);
        _inactiveBullets.remove(bullet);
    }
    
    /*
    Fetches the first inactive Bullet. When trying to fetch a Bullet and none
    are available, a new Bullet instantiated instead.
    */
    public Bullet getBullet() {
        Bullet fetchedBullet;
        
        try {
            // Get first Bullet of inactive pool.
            fetchedBullet = _inactiveBullets.getFirst();
            // Remove first Bullet from inactive pool.
            _inactiveBullets.remove(0);
        } catch(NoSuchElementException e) {
            // Create new Bullet
            fetchedBullet = new Bullet(app, this);
            
            System.out.println(_inactiveBullets.size() + _activeBullets.size() + " wa wa");
        } 
        
        // Add Bullet to active pool.
        _activeBullets.add(fetchedBullet);
        
        return fetchedBullet;
    }
}
