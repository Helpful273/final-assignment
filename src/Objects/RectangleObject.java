package Objects;
import processing.core.*;

/**
 *
 * @author Helpful273
 */
public class RectangleObject {
    // DEFAULTS
    private final static int DEFAULT_COLOUR = 100;
    
    // properties
    public int x, y;
    private int width, height;
    private int[] colourRGB = {DEFAULT_COLOUR, DEFAULT_COLOUR, DEFAULT_COLOUR};
    
    // core
    PApplet app;
    PImage image;
    
    /*
    Constructor
    @param app The parent applet
    @param x the initial X position.
    @param y the initial Y position.
    */
    public RectangleObject(PApplet app, int x, int y) {
        this.app = app;
        this.x = x;
        this.y = y;
    }
    
    /*
    Updates the image of the object.
    @param imagePath The path to the image file.
    */
    public void setImage(String imagePath) {
        image = app.loadImage(imagePath);
    }
    
    /*
    Updates the colour property of the  object.
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
    
    /*
    checks if a point is within the bounds of the rectangle object
    @return if it is within the object.
    */
    public boolean withinInBounds(int x, int y) {
        // if an image exists we will check if the position is within the image
        if (image != null) {
            return (x > this.x - image.width/2 && 
                    x < this.x + image.width/2 && 
                    y > this.y - image.height/2 &&
                    y < this.y + image.height/2);
        }
        
        // if an image does not exist we will check if the position is within
        // rectangular bounds instead.
        return (x > this.x - width/2 && 
                x < this.x + width/2 && 
                y > this.y - height/2 && 
                y < this.y + height/2);
    }
    
    /*
    draws the rectangle object
    */
    public void draw() {
        // if there is an image draw that
        if (image != null) {
            app.imageMode(PConstants.CENTER);
            app.image(image, x, y);
            return;
        }
        
        // if there is no image draw a rectangle instead
        app.fill(colourRGB[0], colourRGB[1], colourRGB[2]);
        app.rectMode(PConstants.CENTER);
        app.rect(x, y, width, height);
    }
}
