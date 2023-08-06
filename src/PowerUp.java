import Enums.PowerUps;
import bagel.Image;
import bagel.Window;
import bagel.util.Point;
import bagel.util.Rectangle;

public abstract class PowerUp {
    protected Point pos;

    public PowerUp() {
        pos = new Point(Window.getWidth() * Math.random(), -200);
    }

    public PowerUp(Point pos) {
        this.pos = pos;
    }

    abstract PowerUps getType();
    public void setPos(Point pos) {
        this.pos = pos;
    }
    public Point getPos() {return pos;}

    abstract void move();
    abstract Rectangle getRectangle();
    abstract void draw();
    abstract void draw(double x, double y); 
    abstract void activate(Character character);

    abstract void adjustOffset(double offset);
    abstract double getSpeed();
}
