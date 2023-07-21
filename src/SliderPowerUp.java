import bagel.Drawing;
import bagel.Image;
import bagel.Input;
import bagel.MouseButtons;
import bagel.util.Colour;
import bagel.util.Point;
import bagel.util.Rectangle;

public class SliderPowerUp extends Slider {

    private final GameSettingsSingleton gameSettingsSingleton = GameSettingsSingleton.getInstance();
    private final PowerUpsSettingsSingleton powerUpsSettingsSingleton = gameSettingsSingleton.getPowerUpsSettingsSingleton();


    private Image logo;
    private String name;
    private String type = "PowerUp";
    private Rectangle slide;
    private Point topLeft;
    private final double minimumFrequency = 0.002;
    private final double maxFrequency = 0.005;
    private final double maxBSize = 1220;
    private int width = 50;

    public SliderPowerUp(String name, Point topLeft) {
        this.logo = new Image(String.format("res/PowerUp/%s.png", name));
        this.name = name;
        this.topLeft = topLeft;
        this.slide = new Rectangle(topLeft, maxBSize, width);
    }

    public String getName() {
        return name;
    }

    public void draw() {
        double currentBar;
        double currentFrequency;
        Image sliderIndicator = new Image("res/misc/sliderIndicatorS.png");
        currentFrequency = powerUpsSettingsSingleton.getFrequency(name);
        currentBar = (currentFrequency - minimumFrequency)/(maxFrequency - minimumFrequency) * maxBSize;
        if (gameSettingsSingleton.getPowerUpsSettingsSingleton().isPowerUp(name)) {
            Drawing.drawRectangle(topLeft.x, topLeft.y + logo.getHeight()/4, maxBSize, 50, new Colour(0, 0, 0, 0.5));
            Drawing.drawRectangle(topLeft.x, topLeft.y + logo.getHeight()/4, currentBar, 50, new Colour((1 - currentBar/maxBSize), 1, currentBar/maxBSize, currentBar/maxBSize));
            sliderIndicator.drawFromTopLeft(currentBar + topLeft.x - 25, topLeft.y + 10);
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
                powerUpsSettingsSingleton.changeFrequency(name, newFrequency);
            }
        }
        if (input.wasPressed(MouseButtons.LEFT)) {
            if (logo.getBoundingBoxAt(new Point(topLeft.x - logo.getWidth()/2
                    , topLeft.y + logo.getHeight()/2)).intersects(input.getMousePosition())) {
                powerUpsSettingsSingleton.toggle(name);
            }
        }
    }

    public void setPos(double x, double y) {
        this.topLeft = new Point(x, y);
    }
}
