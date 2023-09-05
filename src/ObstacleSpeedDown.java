import Enums.Obstacles;
import Enums.PowerUps;
import bagel.Image;
import bagel.Window;
import bagel.util.Point;
import bagel.util.Rectangle;

public class ObstacleSpeedDown extends Obstacle {
    private final ImagePoint image = new ImagePoint("res/Obstacles/SpeedDown.png",
            new Point(Window.getWidth() * Math.random(), -600));
    private double offset = 0;
    private final double speed = 3 + GameSettingsSingleton.getInstance().getMapSpeed();

    public void move() {
        image.move(0, speed + offset);
    }

    public ImagePoint getImage() {
        return image;
    }

    public Rectangle getBoundingBox() { return image.getRectangle();
    }

    public Point getPos() {
        return image.getPos();
    }

    public void setPos(Point point) {
        image.setPos(point);
    }

    public void adjustOffset(double offset) {
        this.offset += offset;
    }

    public double getSpeed() {
        return speed;
    }

    public void draw() {
        image.draw();
    }

    public void draw(double x, double y) {
        image.draw(x, y);
    }

    public void collide(Character character) {
        MusicPlayer.getInstance().addMusic("music/misc/speedDown.wav");
        character.speedDown();
    }

}
