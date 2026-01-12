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
    private static HashMap<String, Stage> stages;
    private static Stage stage;
    
    public static void awake() {
        stages = new HashMap<>();
        
        Stage menu = new Stage() {
            private boolean downThisFrame = false;
            
            @Override
            public void keyHook(int keyCode) {
                if (keyCode == 0) return;
                
                switch(keyCode) {
                    case PConstants.UP:
                        System.out.println(1);
                        break;
                    case PConstants.DOWN:
                        System.out.println(2);
                        break;
                }
            }
        };
        
        stages.put("Menu", menu);
        
        switchStage("Menu");
    }
    
    public static void update(int keyCode) {
        stage.keyHook(keyCode);
        stage.update();
        
    }
    
    public static void switchStage(String newStage) {
        stage = stages.get(newStage);
    }
}
