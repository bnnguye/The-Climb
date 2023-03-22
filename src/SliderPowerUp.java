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
    private String type;
    private Rectangle slide;
    private Point topLeft;
    private final double minimumFrequency = 0.002;
    private final double maxFrequency = 0.005;
    private final double maxBSize = 1220;


    public String getName() {
        return name;
    }

    public void draw() {
        double currentBar;
        double currentFrequency;
        Image sliderIndicator = new Image("res/misc/sliderIndicatorS.png");
        currentFrequency = powerUpsSettingsSingleton.getFrequency(name);
        currentBar = (currentFrequency - minimumFrequency)/(maxFrequency - minimumFrequency) * maxBSize;
        if (gameSettingsSingleton.getObstaclesSettingsSingleton().isObstacle(name)) {
            Drawing.drawRectangle(topLeft, maxBSize, logo.getHeight(), new Colour(0, 0, 0, 0.5));
            Drawing.drawRectangle(topLeft, currentBar, logo.getHeight(), new Colour((1 - currentBar/maxBSize), 1, currentBar/maxBSize, currentBar/maxBSize));
            sliderIndicator.drawFromTopLeft(currentBar + topLeft.x - sliderIndicator.getWidth()/2, topLeft.y - 10);
        }

        else if ("mainVolume".equals(type)) {
            currentFrequency = MusicPlayer.getInstance().getMainMusic().getVolume();
            currentBar = (currentFrequency - minimumFrequency)/(maxFrequency - minimumFrequency) * maxBSize;
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
