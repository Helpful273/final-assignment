package Game;
import Systems.Stages.StageManager;
import processing.core.PApplet;
import Objects.*;
import Utils.*;

/**
 *
 * @author Helpful273
 */
public class Main extends PApplet {
    StageManager stageManager;
    
    public void settings() {
        size(800, 800);
    }
    
    public void setup() {
        stageManager = new StageManager(this);
        stageManager.awake();
    }
    
    public void draw() {
        background(200);
        stageManager.update();
        stageManager.draw();
    }
    
    // events
    public void keyPressed() {
        stageManager.keyDown(keyCode);
    }
    
    public void keyReleased() {
        stageManager.keyUp(keyCode);
    }
}
