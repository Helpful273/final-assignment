package Objects;
import processing.core.*;
import java.util.ArrayList;
import java.util.function.Consumer;


/**
 *
 * @author Helpful273
 */
public class Actor extends MovingObject {
    // CONSTANTS
    private final static int[] HITBOX_COLOUR = {255, 255, 255};
    
    // position
    public int moveFactorX, moveFactorY;
    
    // character properties
    private int health;
    
    // core
    private PApplet app;
    private PImage image;
    private ArrayList<ArrayList<Bullet>> collisionChecks = new ArrayList<>();
    
    /*
    Constructor for circle collider.
    @param app The parent applet.
    @param x The initial x position.
    @param y The initial y position.
    @param radius The initial radius.
    */
    public Actor(PApplet app, String imagePath, int health, int x, int y, int radius) {
        super(app, x, y, radius);
        super.setColour(HITBOX_COLOUR);
        this.app = app;
        this.image = app.loadImage(imagePath);
        this.health = health;
    }
    private final Consumer<Bullet> checkCollision = bullet -> {
        if (this.isColliding(bullet)) {
            this.takeDamage(bullet.getDamage());
            bullet.toKill = true;
        }
    };
    
    private final Consumer<ArrayList<Bullet>> checkPool = pool -> {
        pool.forEach(checkCollision);
    };
    
    public void takeDamage(int damage) {
        health -= damage;
        
        if (health <= 0) {
            // TODO: death hook
        } 
    }
    
    public void addCollisionListener(ArrayList<Bullet> pool) {
        if (collisionChecks.contains(pool)) return;
        collisionChecks.add(pool);
    }
    
    /*
    Updates the character
    */
    @Override
    public void update() {
        if (!collisionChecks.isEmpty())
            collisionChecks.forEach(checkPool);
        
        super.update();
    }
    
    /*
    Draws the character.
    */
    @Override
    public void draw() {
        app.image(image, x - image.width/2, y - image.height/2);
    }
    
    /*
    Draws the character's hitbox.
    */
    public void drawHitbox() {
        super.draw();
    }
}
