import Enums.Obstacles;
import bagel.DrawOptions;
import bagel.Image;
import bagel.Window;
import bagel.util.Point;
import bagel.util.Rectangle;

public class ObstacleRock extends Obstacle {
    private final Obstacles type = Obstacles.ROCK;
    private final Image image = new Image("res/obstacles/Rock.png");
    private final double speed = 3 + GameSettingsSingleton.getInstance().getMapSpeed();

    public ObstacleRock(Point point) {
        this.pos = point;
    }

    public ObstacleRock() {
        super();
    }

    public void move() {
        this.pos = new Point(pos.x, pos.y + speed + offset);
    }

    public Image getImage() {
        return image;
    }

    public Rectangle getBoundingBox() {
        return new Rectangle(pos, image.getWidth(), image.getHeight());
    }

    public Point getPos() {
        return pos;
    }

    public void setPos(Point point) {
        this.pos = point;
    }

    public Obstacles getType() {
        return type;
    }

    public void adjustOffset(double offset) {
        this.offset += offset;
    }

    public double getSpeed() {
        return speed;
    }

    public void draw() {
        image.drawFromTopLeft(pos.x, pos.y);
    }

    public void draw(double x, double y) {
        image.drawFromTopLeft(pos.x + x, pos.y + y);
    }

    public void collide(Character character) {
        character.reduceLive();
    }
}
