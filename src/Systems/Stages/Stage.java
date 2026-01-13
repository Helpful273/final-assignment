package Systems.Stages;

/**
 *
 * @author Helpful273
 */
abstract class Stage {
    public boolean keyAlreadyDown = false;
    
    // creation
    public abstract void awakeFn();
    
    // frame
    public abstract void update();
    public abstract void draw();
}
