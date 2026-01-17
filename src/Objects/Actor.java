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
    private int maxHealth;
    private int health;
    
    // core
    private PApplet app;
    private PImage image;
    private final ArrayList<Double> thresholds = new ArrayList<>();
    
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
        this.maxHealth = health;
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
    
    public void addStage(double stageThreshold) {
        thresholds.add(stageThreshold);
    }
    
    public int getStage() {
        double percentage = (double) health / (double) maxHealth;
        int counter = thresholds.size() + 1;
        for (double threshold: thresholds) {
            if (percentage >= threshold) counter--;
        }
        
        return counter;
    }
    
    public int getHealth() {
        return health;
    }
    
    public void deathHook() {
        // replace
    }
    
    public void takeDamage(int damage) {
        health -= damage;
        
        System.out.println(health);
        
        if (health <= 0) {
            deathHook();
        } 
    }
    
    public void checkCollision(BulletPool bulletPool) {
        bulletPool.getActivePool().forEach(checkCollision);
    }
    
    /*
    Draws the character.
    */
    @Override
    public void draw() {
        app.imageMode(PConstants.CENTER);
        app.image(image, x, y);
    }
    
    /*
    Draws the character's hitbox.
    */
    public void drawHitbox() {
        super.draw();
    }
}
