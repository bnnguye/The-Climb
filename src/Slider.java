import bagel.*;
import bagel.util.Colour;
import bagel.util.Point;
import bagel.util.Rectangle;

public class Slider {

    private final ObstaclesSettingsSingleton obstaclesSettingsSingleton = ObstaclesSettingsSingleton.getInstance();
    private final GameSettingsSingleton gameSettingsSingleton = GameSettingsSingleton.getInstance();
    private final PowerUpsSettingsSingleton powerUpsSettingsSingleton = gameSettingsSingleton.getPowerUpsSettingsSingleton();

    private final Image logo;
    private final String name;
    private final String type;
    private final Rectangle slide;
    private Point topLeft;
    private final double minimumFrequency = 0.003;
    private final double maxFrequency = 0.006;
    private final double maxBSize = 500;

    public Slider(String name, String type, Point topLeft) {
        this.name = name;
        this.type = type;
        this.logo = new Image(String.format("res/%s/%s.png", type, name));
        this.slide = new Rectangle(topLeft, maxBSize, logo.getHeight());
        this.topLeft = topLeft;
    }

    public String getName() {
        return name;
    }

    public void draw() {
        double currentBar;
        double currentFrequency;
        if (("obstacle").equalsIgnoreCase(type)) {
            currentFrequency = obstaclesSettingsSingleton.getFrequency(name);
            currentBar = (currentFrequency - minimumFrequency)/(maxFrequency - minimumFrequency) * maxBSize;
            if (gameSettingsSingleton.getObstaclesSettingsSingleton().isObstacle(name)) {
                Drawing.drawRectangle(topLeft, maxBSize, logo.getHeight(), new Colour(0, 0, 0, 0.5));
                Drawing.drawRectangle(topLeft, currentBar, logo.getHeight(), new Colour(0, 1, 0.7));
            }
        }
        else if (("powerup").equalsIgnoreCase(type)) {
            currentFrequency = powerUpsSettingsSingleton.getFrequency(name);
            currentBar = (currentFrequency - minimumFrequency)/(maxFrequency - minimumFrequency) * maxBSize;
            if (gameSettingsSingleton.getPowerUpsSettingsSingleton().isPowerUp(name)) {
                Drawing.drawRectangle(topLeft, maxBSize, logo.getHeight(), new Colour(0, 0, 0, 0.5));
                Drawing.drawRectangle(topLeft, currentBar, logo.getHeight(), new Colour(0, 1, 0.7));
            }
        }
        logo.drawFromTopLeft(topLeft.x - logo.getWidth(), topLeft.y);
    }

    public void interact(Input input) {
        if (input.wasPressed(MouseButtons.LEFT)) {
            double mouseX = input.getMouseX();
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
                System.out.println(newFrequency);
                if (type.equalsIgnoreCase("obstacle")) {
                     obstaclesSettingsSingleton.changeFrequency(name, newFrequency);
                }
                else if (type.equalsIgnoreCase("powerup")) {
                    powerUpsSettingsSingleton.changeFrequency(name, newFrequency);
                }
            }
            else if (logo.getBoundingBoxAt(new Point(topLeft.x - logo.getWidth()/2
                    , topLeft.y + logo.getHeight()/2)).intersects(input.getMousePosition())) {
                if (type.equalsIgnoreCase("obstacle")) {
                    obstaclesSettingsSingleton.toggle(name);
                }
                else if (type.equalsIgnoreCase("powerup")) {
                    powerUpsSettingsSingleton.toggle(name);
                }
            }
        }
    }

    public void setPos(double x, double y) {
        this.topLeft = new Point(x, y);
    }


}
