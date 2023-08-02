import bagel.Image;
import bagel.Input;
import bagel.util.Point;
import bagel.util.Rectangle;

public abstract class Slider {

    public abstract void draw();

    public abstract void interact(Input input);

    public abstract void setPos(double x, double y);

}
