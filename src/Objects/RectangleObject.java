package Objects;
import processing.core.PApplet;
import processing.core.PImage;

/**
 *
 * @author Helpful273
 */
public class RectangleObject {
    // properties
    public int x, y;
    private int width, height;
    private int[] colourRGB;
    
    // core
    PApplet app;
    PImage image;
    
    public RectangleObject(PApplet app, int x, int y) {
        this.app = app;
        this.x = x;
        this.y = y;
    }
    /*
    Updates the colour property of the moving object.
    @param colourRGB The colour in an RGB format.
    */
    public void setColour(int[] colourRGB) {
        this.colourRGB = colourRGB;
    }
    
    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
    }
    
    /*
    Returns the colour property of the moving object.
    @return The clouur in RGB format.
    */
    public int[] getColour() {
        return colourRGB;
    }
    
    public boolean withinInBounds(int x, int y) {
        return (x > this.x - width/2 && x < this.x + width/2 && y > this.y - height/2 && y < this.y + height/2);
    }
    
    public void draw() {
        app.fill(colourRGB[0], colourRGB[1], colourRGB[2]);
        app.rect(x - width/2, y - height/2, width, height);
    }
}
