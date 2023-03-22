import bagel.Image;
import bagel.Input;
import bagel.util.Point;
import bagel.util.Rectangle;

public abstract class Slider {

    private Image logo;
    private String name;
    private String type;
    private Rectangle slide;
    private Point topLeft;

    public abstract void draw();

    public abstract void interact(Input input);

    public abstract void setPos(double x, double y);

    public abstract String getName();

}
