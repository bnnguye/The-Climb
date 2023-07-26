import bagel.Image;
import bagel.Window;
import bagel.util.Point;

public abstract class PowerUp {
    protected Point pos;

    public PowerUp() {
        pos = new Point(Window.getWidth() * Math.random(), -200);
    }

    abstract void move();
    abstract Image getImage();
    abstract Point getPos();
    abstract String getName();
    abstract void gainPowerUp(Player player);
    abstract void activate(Character character);

    abstract void adjustOffset(double offset);
    abstract double getSpeed();
}
