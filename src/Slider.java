import bagel.*;
import bagel.util.Colour;
import bagel.util.Point;
import bagel.util.Rectangle;

public class Slider {
    private Image logo;
    private String name;
    private Rectangle slide;
    private Point topLeft;
    private double currentBar;

    public Slider(String name, String type, Point topLeft) {
        this.name = name;
        logo = new Image(String.format("res/%s/%s.png", type, name));
        slide = new Rectangle(topLeft, Window.getWidth() - logo.getWidth()*2, logo.getHeight());
        this.topLeft = topLeft;
        currentBar = ObstaclesSettingsSingleton.getInstance().getFrequency(name)*500;
    }

    public String getName() {
        return name;
    }

    public void draw() {
        logo.drawFromTopLeft(topLeft.x - logo.getWidth(), topLeft.y);
        if (GameSettingsSingleton.getInstance().getPowerUpsSettingsSingleton().isPowerUp(name)) {
            Drawing.drawRectangle(topLeft, 500, logo.getHeight(), new Colour(0, 0, 0, 0.5));
            Drawing.drawRectangle(topLeft, currentBar,
                    logo.getHeight(), new Colour(0, 1, 0));
        }
    }

    public void interact(Input input) {
//        if (GameSettingsSingleton.getInstance())
        if ((slide.intersects(input.getMousePosition())) && (input.wasPressed(MouseButtons.LEFT))) {
            currentBar = input.getMouseX() - slide.left();
            double minimumFrequency = ObstaclesSettingsSingleton.getInstance().getMinimumFrequency();
            ObstaclesSettingsSingleton.getInstance().changeFrequency(name,
                    minimumFrequency + (1 - minimumFrequency) *  ((slide.right() - slide.left())/500)
                    );
        }
        else if ((input.wasPressed(MouseButtons.LEFT)) && logo.getBoundingBoxAt(
                new Point(topLeft.x - logo.getWidth(), topLeft.y + logo.getHeight()/2)).intersects(
                        input.getMousePosition())) {
            GameSettingsSingleton.getInstance().getPowerUpsSettingsSingleton().toggle(name);
        }
    }

    public void setPos(double x, double y) {
        this.topLeft = new Point(x, y);
    }


}
