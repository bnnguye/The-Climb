import bagel.DrawOptions;
import bagel.Drawing;
import bagel.Image;
import bagel.util.Colour;
import bagel.util.Point;
import bagel.util.Rectangle;


public class CollisionBlock {
    DrawOptions drawOptions = new DrawOptions();
    Image rock = new Image("res/misc/Collision.png");
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
        if (width < height) {
            drawOptions.setRotation(90/360d);
        }
    }

    public void updatePos(Point point) {
        this.pos = new Point(this.pos.x, point.y);
        this.rectangle = new Rectangle(new Point(this.pos.x, point.y), this.width, this.height);
    }

    public void draw() {
//        Drawing.drawRectangle(pos, this.width, this.height, color);
        if (width < height) {
            drawOptions.setRotation(570/360d);
            rock.drawFromTopLeft(pos.x - rock.getWidth()/2, pos.y + rock.getWidth()/2 - 40, drawOptions);
        }
        else {
            rock.drawFromTopLeft(pos.x, pos.y - rock.getHeight()/2);
        }
    }

    public Rectangle getRectangle() {return this.rectangle;}

    public boolean hasCollided(Rectangle rectangle) {
        return rectangle.intersects(this.rectangle);
    }

    public Point getPos() { return this.pos;}

    public void setPos(Point point) { this.pos = point;}

}
