import Enums.PowerUps;
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
    private final PowerUps type;
    private Rectangle slide;
    private Point topLeft;
    private final double minimumFrequency = 0.002;
    private final double maxFrequency = 0.04;
    private final double maxBSize = 1220;
    private int width = 50;

    public SliderPowerUp(PowerUps type, Point topLeft) {
        this.type = type;
        this.logo = new Image(String.format("res/PowerUps/%s.png", type.toString()));
        this.topLeft = topLeft;
        this.slide = new Rectangle(topLeft, maxBSize, width);
    }

    public void draw() {
        double currentBar;
        double currentFrequency;
        currentFrequency = powerUpsSettingsSingleton.getFrequency(type);
        currentBar = (currentFrequency - minimumFrequency)/(maxFrequency - minimumFrequency) * maxBSize;

        if (gameSettingsSingleton.getPowerUpsSettingsSingleton().isPowerUp(type)) {
            Drawing.drawRectangle(topLeft.x, topLeft.y + logo.getHeight()/4, maxBSize, 50, new Colour(0, 0, 0, 0.3));
            Drawing.drawRectangle(topLeft.x, topLeft.y + logo.getHeight()/4, currentBar, 50,
                    new Colour( (1 - (maxBSize - currentBar)/maxBSize)*25.5/100 + 106.7/255d,
                            (1 - (maxBSize - currentBar)/maxBSize)*25.5/100 + 143.9/255d,
                            (1 - (maxBSize - currentBar)/maxBSize)*25.5/100 + 121.6/255d));
            drawSliderIndicator(logo.getHeight()*1.1, currentBar + topLeft.x, topLeft.y - logo.getHeight()/2);
        }
        logo.drawFromTopLeft(topLeft.x - logo.getWidth()/4, topLeft.y);
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
                powerUpsSettingsSingleton.changeFrequency(type, newFrequency);
            }
        }
        if (input.wasPressed(MouseButtons.LEFT)) {
            if (logo.getBoundingBoxAt(new Point(topLeft.x + logo.getWidth()/4
                    , topLeft.y + logo.getHeight()/2)).intersects(input.getMousePosition())) {
                powerUpsSettingsSingleton.toggle(type);
            }
        }
    }

    public void setPos(double x, double y) {
        this.topLeft = new Point(x, y);
    }

    private void drawSliderIndicator(double size, double x, double y) {
        Drawing.drawRectangle(x,y + size/2, 11, size + 10, new Colour(0.7,0.7,0.7,1));
        Drawing.drawRectangle(x + 2.75,y + size/2 + 4, 5, size, new Colour(0.2,0.2,0.2,1));
    }
}
