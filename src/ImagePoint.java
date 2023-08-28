import bagel.DrawOptions;
import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;

/**
 * The class ImagePoint acts as a container/extension to bagel.Image
 * filename::String:: name of image file
 * pos::Point:: top-left x,y position of the image
 */
public class ImagePoint {
    private String filename;
    private String tag = null;
    private int priority = 0;
    private Point pos;
    private DrawOptions DO;
    private double scale = 1;

    private boolean flashing = false;
    private double opacity = 1;
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
                DO.setBlendColour(0.5, 0.5, 0.5,opacity);
            }
            else if (darken) {
                DO.setBlendColour(0,0,0,opacity);
            }
            else {
                DO.setBlendColour(1,1,1,opacity);
            }
            new Image(filename).drawFromTopLeft(pos.x - ((getWidth() * (1 - scale))/2), pos.y - ((getHeight() * (1 - scale))/2), DO);
        }
    }

    public void draw(double x, double y) {
        if (filename != null) {
            if (flashing) {
                DO.setBlendColour(0.5, 0.5, 0.5,opacity);
            }
            else if (darken) {
                DO.setBlendColour(0,0,0,opacity);
            }
            else {
                DO.setBlendColour(1,1,1,opacity);
            }
            new Image(filename).drawFromTopLeft(pos.x - ((getWidth() * (1 - scale))/2) + x, pos.y - ((getHeight() * (1 - scale))/2) + y, DO);
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

    public void setOpacity(double opacity) {
        this.opacity = opacity;
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

    public void move(double x, double y) {
        this.pos = new Point(pos.x + x, pos.y + y);
    }

    public void setColour(double r, double g, double b) {
        DO.setBlendColour(r, g, b, opacity);
    }

    public Rectangle getRectangle() {
        return new Rectangle(this.pos, getWidth() * scale, getHeight() * scale);
    }

}
