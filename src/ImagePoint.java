import bagel.DrawOptions;
import bagel.Image;
import bagel.util.Point;

/**
 * The class ImagePoint acts as a container/extension to bagel.Image
 * filename::String:: name of image file
 * pos::Point:: top-left x,y position of the image
 */
public class ImagePoint {
    private String filename;
    private String tag = null;
    private Point pos;
    private DrawOptions DO;
    private double scale = 1;

    private boolean flashing = false;
    private boolean transparent = false;
    private boolean shaking = false;
    private boolean darken = false;
    private boolean scaled = false;

    public ImagePoint(String filename, Point pos) {
        this.filename = filename;
        this.pos = pos;
        DO = new DrawOptions();
    }

    public ImagePoint(String filename, Point pos, String tag) {
        this.filename = filename;
        this.pos = pos;
        this.tag = tag;
        DO = new DrawOptions();
    }

    // Draws from top-left as point
    public void draw() {
        if (filename != null) {
            if (flashing) {
                DO.setBlendColour(0.5, 0.5, 0.5,0.7);
            }
            else if (transparent) {
                DO.setBlendColour(1,1,1,0.3);
            }
            else if (darken) {
                DO.setBlendColour(0,0,0,1);
            }
            new Image(filename).drawFromTopLeft(pos.x - ((getWidth() * (1 - scale))/2), pos.y - ((getHeight() * (1 - scale))/2), DO);
        }
    }

    public void setImage(String filename) {
        this.filename = filename;
    }

    public String getFilename() {
        return this.filename;
    }

    public void setPos(double x, double y) {
        this.pos = new Point(x,y);
    }

    public void setPos(Point point) {
        this.pos = point;
    }

    public double getWidth() {
        return new Image(this.filename).getWidth();
    }

    public double getHeight() {
        return new Image(this.filename).getHeight();
    }

    public void setScale(double scale) {
        this.scale = scale;
        DO.setScale(scale, scale);
    }

    public void setSection(double xBegin, double yBegin, double width, double height) {
        DO.setSection(xBegin, yBegin, width, height);
    }

    public double getScale() {return this.scale;}

    public Point getPos() {
        return pos;
    }

    public boolean isFlashing() {
        return flashing;
    }

    public void setFlashing(boolean flashing) {
        this.flashing = flashing;
    }

    public boolean isTransparent() {
        return transparent;
    }

    public void setTransparent(boolean transparent) {
        this.transparent = transparent;
    }

    public void setDarken(boolean darken) {
        this.darken = darken;
    }

    public boolean getDarken() { return darken;}

    public String getTag() {
        return this.tag;
    }

    public DrawOptions getDO() {
        return DO;
    }

}
