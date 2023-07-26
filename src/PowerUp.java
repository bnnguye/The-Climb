import bagel.Image;
import bagel.Window;
import bagel.util.Point;
import bagel.util.Rectangle;

public abstract class PowerUp {
    protected Point pos;

    public PowerUp() {
        pos = new Point(Window.getWidth() * Math.random(), -200);
    }

    abstract void move();
    abstract Rectangle getRectangle();
    abstract void draw();
    abstract void activate(Character character);

    abstract void adjustOffset(double offset);
    abstract double getSpeed();
}
