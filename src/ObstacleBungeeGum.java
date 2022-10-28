import bagel.Drawing;
import bagel.Image;
import bagel.util.Colour;
import bagel.util.Point;

public class ObstacleBungeeGum {
    private String direction;
    private Point pos;
    private final double speed = 6 + GameSettingsSingleton.getInstance().getMapSpeed();
    private Image image = new Image("res/charactersS/Hisoka/BungeeGum.png");

    public ObstacleBungeeGum(Point point, String direction) {
        this.direction = direction;
        this.pos = point;
    }

    public void move() {
        Point newPoint = this.pos;
        if (direction.equals("N")) {
            newPoint = new Point(this.pos.x, this.pos.y - speed);
        }
        else if (direction.equals("S")) {
            newPoint = new Point(this.pos.x, this.pos.y + speed);
        }
        else if (direction.equals("W")) {
            newPoint = new Point(this.pos.x - speed, this.pos.y);
        }
        else if (direction.equals("E")) {
            newPoint = new Point(this.pos.x + speed, this.pos.y);
        }
        else if (direction.equals("NW")) {
            newPoint = new Point(this.pos.x - speed, this.pos.y - speed);
        }
        else if (direction.equals("NE")) {
            newPoint = new Point(this.pos.x + speed, this.pos.y - speed);
        }
        else if (direction.equals("SW")) {
            newPoint = new Point(this.pos.x - speed, this.pos.y + speed);
        }
        else if (direction.equals("SE")) {
            newPoint = new Point(this.pos.x + speed, this.pos.y + speed);
        }
        this.pos = newPoint;
    }

    public void draw() {
        image.drawFromTopLeft(this.pos.x, this.pos.y);
    }

    public Point getPos() {return this.pos;}

    public Image getImage() {
        return image;
    }


}
