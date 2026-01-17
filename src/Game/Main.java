package Game;
import Systems.StageManager;
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
    
    public void mousePressed() {
        stageManager.mouseDown(mouseX, mouseY, mouseButton);
    }
    
    public void mouseReleased() {
        stageManager.mouseUp();
    }
}
