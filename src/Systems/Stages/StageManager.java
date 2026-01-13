package Systems.Stages;
import Objects.*;
import Utils.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import processing.core.PApplet;
import processing.core.PConstants;


/**
 *
 * @author Helpful273
 */
public class StageManager {
    // properties
    private HashMap<String, Stage> stages;
    private Stage stage = null;
    
    // core
    private PApplet app;
    private HashSet<Integer> keysPressed = new HashSet<>();
    
    public StageManager(PApplet app) {
        this.app = app;
    }
    
    public void awake() {
        stages = new HashMap<>();
        
        Stage menu = new Stage() {
            private MovingObject pointer = null;
            
            public void awakeFn() {
                if (pointer == null) {
                    pointer = new MovingObject(app, 400, 400, 20);
                    pointer.setSpeed(5);
                }
            }
            
            public void update() {
                int moveX = 0;
                int moveY = 0;
                double diminFactor = 1;
                
                if (keysPressed.contains(PConstants.LEFT)) {
                    moveX -= 1;
                }
                
                if(keysPressed.contains(PConstants.RIGHT)) {
                    moveX += 1;
                }
                
                if (keysPressed.contains(PConstants.UP)) {
                    moveY -= 1;
                }
                
                if(keysPressed.contains(PConstants.DOWN)) {
                    moveY += 1;
                }
                
                if (moveX != 0 && moveY != 0) diminFactor = Math.sqrt(2);
                
                pointer.x += moveX * pointer.getSpeed() / diminFactor;
                pointer.y += moveY * pointer.getSpeed() / diminFactor;
            }
            
            public void draw() {
                pointer.draw();
            }
        };
        
        stages.put("Menu", menu);
        
        switchStage("Menu");
    }
    
    public void update() {
        stage.update();
    }
    
    public void draw() {
        stage.draw();
    }
    
    public void keyDown(int keyCode) {
        keysPressed.add(keyCode);
    }
    
    public void keyUp(int keyCode) {
        keysPressed.remove(keyCode);
    }
    
    public void switchStage(String newStage) {
        stage = stages.get(newStage);
        stage.awakeFn();
    }
}
