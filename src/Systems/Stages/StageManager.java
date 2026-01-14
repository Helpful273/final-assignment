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
    private HashMap<String, Stage> stages = new HashMap<>();;
    private Stage stage = null;
    
    // core
    private PApplet app;
    private HashSet<Integer> keysPressed = new HashSet<>();
    
    public StageManager(PApplet app) {
        this.app = app;
    }
    
    public void awake() {
        stages.put("Menu", menu);
        stages.put("combat1", combat1);
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
    
    Stage menu = new Stage() {
        private boolean init = false;
        private MovingObject pointer = null;
        private RectangleObject startButton = null;
            
        public void awakeFn() {
            if (init) return;
            init = true;
                    
            pointer = new MovingObject(app, 400, 400, 20);
            pointer.setSpeed(5);
                    
            startButton = new RectangleObject(app, 400, 700);
            startButton.setColour(new int[] {255, 100, 100});
            startButton.setSize(600, 75);
        }
            
        public void update() {
            startButton.setColour(new int[] {255, 100, 100});
            if (startButton.withinInBounds(pointer.x, pointer.y)) {
                startButton.setColour(new int[] {100, 255, 100});
                switchStage("combat1");
            }
            
            int moveX = 0;
            int moveY = 0;
            double diminFactor = 1;
                
            if (keysPressed.contains(PConstants.LEFT)) moveX -= 1;
            if (keysPressed.contains(PConstants.RIGHT)) moveX += 1;
            if (keysPressed.contains(PConstants.UP)) moveY -= 1;
            if (keysPressed.contains(PConstants.DOWN)) moveY += 1;
            if (moveX != 0 && moveY != 0) diminFactor = Math.sqrt(2);
                
            pointer.x += moveX * pointer.getSpeed() / diminFactor;
            pointer.y += moveY * pointer.getSpeed() / diminFactor;
        }
            
        public void draw() {
            startButton.draw();
            pointer.draw();
        }
    };
    
    Stage combat1 = new Stage() {
        public void awakeFn() {
                
        }
        public void update() {
                
        }
        public void draw() {
                
        }
    };
}
