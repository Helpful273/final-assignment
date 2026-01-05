package Objects;
import processing.core.PApplet;
import java.util.ArrayList;
import java.util.NoSuchElementException;

/**
 *
 * @author Helpful273
 */
public class BulletPool {
    private final PApplet app;
    private final ArrayList<Bullet> _activeBullets = new ArrayList();
    private final ArrayList<Bullet> _inactiveBullets = new ArrayList();
    private final static int DEFAULT_INIT_POOL_SIZE = 100;
    
    /*
    Creates a new pool of bullets.
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
    */
    public BulletPool(PApplet app) {
        this(app, DEFAULT_INIT_POOL_SIZE);
    }
    
    /*
    Recalls all bullets in the pool, setting them to an inactive state. 
    */
    public void Recall() {
        _inactiveBullets.addAll(_activeBullets);
        _inactiveBullets.clear();
    }
    
    /*
    Recalls a bullet in the pool, setting them to an inactive state.
    @param bullet The bullet object to recall.
    */
    public void Recall(Bullet bullet) {
        _inactiveBullets.add(bullet);
        _inactiveBullets.remove(bullet);
    }
    
    /*
    Fetches the first inactive Bullet. When trying to fetch a Bullet and none
    are available, a new Bullet instantiated instead.
    */
    public Bullet GetBullet() {
        Bullet fetchedBullet;
        
        try {
            // Get first Bullet of inactive pool.
            fetchedBullet = _inactiveBullets.getFirst();
            // Remove first Bullet from inactive pool.
            _inactiveBullets.remove(0);
        } catch(NoSuchElementException e) {
            // Create new Bullet
            fetchedBullet = new Bullet(app, this);
        } 
        
        // Add Bullet to active pool.
        _activeBullets.add(fetchedBullet);
        
        return fetchedBullet;
    }
}
