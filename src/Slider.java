import bagel.Drawing;
import bagel.Image;
import bagel.Input;
import bagel.Window;
import bagel.util.Colour;
import bagel.util.Point;
import bagel.util.Rectangle;

public class Slider {
    private Image logo;
    private String name;
    private Rectangle slide;
    private Point topLeft;
    private double percentage;

    public Slider(String name, String type) {
        this.name = name;
        this.topLeft = new Point(0,0);
        percentage = 0.5;
    }

    public Slider(String name, String type, Point topLeft) {
        this.name = name;
        logo = new Image(String.format("res/%s/%s.png", type, name));
        slide = new Rectangle(new Point(logo.getWidth(), 0), Window.getWidth() - logo.getWidth()*2, 100);
        this.topLeft = topLeft;
        percentage = 0.5;
    }

    public Image getLogo() {
        return logo;
    }

    public String getName() {
        return name;
    }

    public Rectangle getSlide() {
        return slide;
    }

    public void draw() {

        Drawing.drawRectangle(topLeft, 500, logo.getHeight(), new Colour(0, 0, 0, 0.5));
        Drawing.drawRectangle(topLeft, percentage * slide.right() - slide.left(), logo.getHeight(), new Colour(0, 0, 0));
    }

    public void interact(Input input) {
    }

    public void setPos(double x, double y) {
        this.topLeft = new Point(x, y);
    }

}
