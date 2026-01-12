package Systems.Stages;

/**
 *
 * @author Helpful273
 */
abstract class Stage {
    public boolean keyAlreadyDown = false;
    
    // creation
    public abstract void awakeFn();
    public abstract void cleanFn();
    
    // frame
    public void update() {
        
    }
    
    public void draw() {
        
    }
    
    // events
    public abstract void keyDown(int keyCode);
    
    public void keyUp() {
        keyAlreadyDown = false;
    }
}
