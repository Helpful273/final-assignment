package Systems.Stages;
import Utils.*;
import java.util.ArrayList;
import java.util.HashMap;
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
    
    // helpers
    public ArrayList<Object> objects = new ArrayList<>();
    
    // core
    public PApplet app;
    
    public StageManager(PApplet app) {
        this.app = app;
    }
    
    public void awake() {
        stages = new HashMap<>();
        
        Stage menu = new Stage() {
            public void awakeFn() {
                
            }
            
            public void cleanFn() {
                
            }
            
            public void keyDown(int keyCode) {
                if (keyAlreadyDown) return;
                
                switch(keyCode) {
                    case PConstants.UP:
                        keyAlreadyDown = true;
                        break;
                    case PConstants.DOWN:
                        keyAlreadyDown = true;
                        break;
                }
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
        stage.keyDown(keyCode);
    }
    
    public void keyUp() {
        stage.keyUp();
    }
    
    public void switchStage(String newStage) {
        if (stage != null) {
            stage.cleanFn();
        }
        
        stage = stages.get(newStage);
        stage.awakeFn();
    }
}
