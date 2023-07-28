import Enums.Obstacles;
import bagel.*;
import bagel.util.Colour;
import bagel.util.Point;
import bagel.util.Rectangle;

public class SliderObstacle extends Slider {

    private final ObstaclesSettingsSingleton obstaclesSettingsSingleton = ObstaclesSettingsSingleton.getInstance();
    private final GameSettingsSingleton gameSettingsSingleton = GameSettingsSingleton.getInstance();

    private final Image logo;
    private final Obstacles name;
    private final String type = "Obstacle";
    private final Rectangle slide;
    private Point topLeft;

    private final double minimumFrequency = 0.002;
    private final double maxFrequency = 0.005;
    private final double maxBSize = 1220;

    public SliderObstacle(Obstacles type, Point topLeft) {
        this.name = type;
        this.topLeft = topLeft;

        this.logo = new Image(String.format("res/%s/%s.png", type, name));
        this.slide = new Rectangle(topLeft, maxBSize, logo.getHeight());
    }

    public void draw() {
        double currentFrequency = obstaclesSettingsSingleton.getFrequency(name);
        double currentBar = (currentFrequency - minimumFrequency)/(maxFrequency - minimumFrequency) * maxBSize;
        Image sliderIndicator = new Image("res/misc/sliderIndicatorS.png");
        if (gameSettingsSingleton.getObstaclesSettingsSingleton().isObstacle(name)) {
            Drawing.drawRectangle(topLeft, maxBSize, logo.getHeight(), new Colour(0, 0, 0, 0.5));
            Drawing.drawRectangle(topLeft, currentBar, logo.getHeight(), new Colour((1 - currentBar/maxBSize), 1, currentBar/maxBSize, currentBar/maxBSize));
            sliderIndicator.drawFromTopLeft(currentBar + topLeft.x - sliderIndicator.getWidth()/2, topLeft.y - 10);
        }
        logo.drawFromTopLeft(topLeft.x - logo.getWidth(), topLeft.y);
    }

    public void interact(Input input) {
        double mouseX = input.getMouseX();
        if (input.isDown(MouseButtons.LEFT)) {
            if (slide.intersects(input.getMousePosition())) {
                if (mouseX > topLeft.x + maxBSize) {
                    mouseX = topLeft.x + maxBSize;
                }
                double newFrequency = ((mouseX - topLeft.x)/maxBSize)*(maxFrequency - minimumFrequency) + minimumFrequency;
                if (newFrequency > maxFrequency) {
                    newFrequency = maxFrequency;
                }
                else if (newFrequency < minimumFrequency) {
                    newFrequency = minimumFrequency;
                }
                obstaclesSettingsSingleton.changeFrequency(name, newFrequency);
            }
        }
        if (input.wasPressed(MouseButtons.LEFT)) {
            if (logo.getBoundingBoxAt(new Point(topLeft.x - logo.getWidth()/2
                    , topLeft.y + logo.getHeight()/2)).intersects(input.getMousePosition())) {
                obstaclesSettingsSingleton.toggle(name);
            }
        }
    }

    public void setPos(double x, double y) {
        this.topLeft = new Point(x, y);
    }


}
