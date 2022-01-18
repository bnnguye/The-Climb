import bagel.Image;
import bagel.util.Point;

public class ObstacleArrow{
    private Point position;
    private Image image = new Image("res/charactersS/Senkuu/Arrow.png");
    private double speed = 1 + GameSettingsSingleton.getInstance().getMapSpeed();

    public ObstacleArrow(Point point) {
        this.position = point;
    }

    public void update() {
        position = new Point(position.x, position.y + speed);
    }

    public Image getImage() {
        return image;
    }

    public Point getPos() {return this.position;}

    public void draw() {
        image.drawFromTopLeft(position.x, position.y);
    }
}
