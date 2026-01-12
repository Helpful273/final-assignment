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
    public void settings() {
        size(800, 800);
    }
    
    public void setup() {
        StageManager.awake();
    }
    
    public void draw() {
        StageManager.update(keyCode);
        
    }
}
