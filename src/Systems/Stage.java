package Systems;

/**
 *
 * @author Helpful273
 */
abstract class Stage {
    public boolean init = false;
    
    // creation
    public void awakeFn() {
        if (init) return;
        init = true;
    };
    
    // frame
    public abstract void update();
    public abstract void draw();
}
