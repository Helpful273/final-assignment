package Objects;
import java.util.ArrayList;
import java.util.NoSuchElementException;

/**
 *
 * @author Helpful273
 */
public class BulletPool {
    private ArrayList<Bullet> _activeBullets = new ArrayList();
    private ArrayList<Bullet> _inactiveBullets = new ArrayList();
    
    /*
    Creates a new pool of bullets.
    @param initialAmount The initial amount of bullets to be loaded into the
    cache.
    */
    public BulletPool(int initialAmount) {
        for (int i = 0; i < initialAmount; i++) {
            Bullet newBullet = new Bullet();
            _inactiveBullets.add(newBullet);
        }
    }
    
    /*
    Recalls all bullets in the pool, setting them to a "DEAD" state. 
    */
    public void Recall() {
        _inactiveBullets.addAll(_activeBullets);
        _inactiveBullets.removeAll(_activeBullets);
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
            fetchedBullet = new Bullet();
        } 
        
        // Add Bullet to active pool.
        _activeBullets.add(fetchedBullet);
        
        return fetchedBullet;
    }
}
