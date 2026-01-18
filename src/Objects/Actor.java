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
    
    /*
    Creates a function that is then passed in a loop to check for collision
    */
    private final Consumer<Bullet> checkCollision = bullet -> {
        // check if the Actor object (this) is colliding with a bullet
        if (this.isColliding(bullet)) {
            // if it is take damage and kill the bullet
            this.takeDamage(bullet.getDamage());
            bullet.toKill = true;
        }
    };
    
    /*
    Adds a stage at a health threshold.
    @param The percentage in decimal format of the threshold (0)
    */
    public void addStage(double stageThreshold) {
        thresholds.add(stageThreshold);
    }
    
    /*
    Mainly used for "enemy" actors.
    @return The current "stage"
    */
    public int getStage() {
        // get current percentage of health
        double percentage = (double) health / (double) maxHealth;
        // set default stage to the maximum
        int counter = thresholds.size() + 1;
        
        // for every threshold that can be passed we deduct from the counter
        // this works because its in reverse.
        for (double threshold: thresholds) {
            if (percentage >= threshold) counter--;
        }
        
        return counter;
    }
    
    /*
    @return The health of the actor
    */
    public int getHealth() {
        return health;
    }
    
    /*
    Sets the new health of the actor.
    @param health the new health
    */
    public void setHealth(int health) {
        this.health = health;
    }
    
    /*
    an event that activates on death.
    */
    public void deathHook() {
        //this function itself does nothing but should be overwritten.
    }
    
    /*
    makes the actor take an amount of damage. If the health points of the actor
    falls below zero fire a deathHook event.
    */
    public void takeDamage(int damage) {
        // sub hp
        health -= damage;
        
        // deathhook check
        if (health <= 0) {
            deathHook();
        } 
    }
    
    /*
    check if the actor is colliding with any bullets in a specified pool.
    */
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
