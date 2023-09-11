import Enums.PowerUps;
import bagel.Image;
import bagel.Window;
import bagel.util.Point;
import bagel.util.Rectangle;

public abstract class PowerUp {
    protected Point pos;
    protected double offset = 0;

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

    public double getOffset() {
        return offset;
    }
    public void adjustOffset(double offset) { this.offset += offset;}

    public void setOffset(double offset) {this.offset = offset;}
    abstract double getSpeed();
}
