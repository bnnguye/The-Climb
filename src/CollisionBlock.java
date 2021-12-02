import bagel.Drawing;
import bagel.Image;
import bagel.util.Colour;
import bagel.util.Point;
import bagel.util.Rectangle;


public class CollisionBlock {
    Rectangle rectangle;
    Point pos;
    double width;
    double height;
    Colour color = new Colour(1, 0.3, 0.3);

    public CollisionBlock(Point point, double width, double height) {
        height+=5;
        width+=5;
        point = new Point(point.x - 10, point.y - 100);
        this.rectangle = new Rectangle(new Point(point.x, point.y), width, height);
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

    public boolean hasCollided(Player player) {
        return player.getCharacter().getImage().getBoundingBoxAt(player.getCharacter().getPos()).intersects(this.rectangle);
    }

    public Point getPos() { return this.pos;}

    public void setPos(Point point) { this.pos = point;}

}
