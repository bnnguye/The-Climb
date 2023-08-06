import Enums.Obstacles;
import Enums.PowerUps;
import bagel.Image;
import bagel.Window;
import bagel.util.Point;
import bagel.util.Rectangle;

public class ObstacleSpeedDown extends Obstacle {
    private final Obstacles type = Obstacles.SPEEDDOWN;
    private final Image image = new Image("res/Obstacles/SpeedDown.png");
    private double offset = 0;
    private final double speed = 3 + GameSettingsSingleton.getInstance().getMapSpeed();

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
        character.speedDown();
    }

}
