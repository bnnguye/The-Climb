import bagel.Drawing;
import bagel.Image;
import bagel.util.Colour;
import bagel.util.Point;
import bagel.util.Rectangle;
import org.w3c.dom.css.Rect;


public class CollisionBlock {
    Rectangle rectangle;
    private Point pos;
    double width;
    double height;
    Colour color = new Colour(153.0/255.0, 27.0/255.0, 0);

    public CollisionBlock(Point point, double width, double height) {
        point = new Point(point.x - 10, point.y - 10);
        this.rectangle = new Rectangle(new Point(point.x - 10, point.y - 10), width, height);
        this.width = width;
        this.height = height;
        this.pos = point;
    }

    public void updatePos(Point point) {
        this.pos = new Point(this.pos.x, point.y);
        this.rectangle = new Rectangle(new Point(this.pos.x, point.y), this.width, this.height);
    }

    public void draw() {
        Drawing.drawRectangle(pos, this.width, this.height, color);
    }

    public Rectangle getRectangle() {return this.rectangle;}

    public boolean hasCollided(Rectangle rectangle) {
        return rectangle.intersects(this.rectangle);
    }

    public Point getPos() { return this.pos;}

    public void setPos(Point point) { this.pos = point;}

}
