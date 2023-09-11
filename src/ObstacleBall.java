import Enums.Obstacles;
import bagel.Image;
import bagel.Window;
import bagel.util.Point;
import bagel.util.Rectangle;

import java.util.ArrayList;

public class ObstacleBall extends Obstacle {
    private final ImagePoint image = new ImagePoint("res/obstacles/ball.png",
            new Point(Window.getWidth() * Math.random(), -600));
    private final double speed = 6 + GameSettingsSingleton.getInstance().getMapSpeed();

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
        character.reduceLive();
    }
}

