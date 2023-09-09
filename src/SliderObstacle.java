import Enums.Obstacles;
import Enums.PowerUps;
import bagel.*;
import bagel.util.Colour;
import bagel.util.Point;
import bagel.util.Rectangle;

public class SliderObstacle extends Slider {

    private final ObstaclesSettingsSingleton obstaclesSettingsSingleton = ObstaclesSettingsSingleton.getInstance();
    private final GameSettingsSingleton gameSettingsSingleton = GameSettingsSingleton.getInstance();

    private ImagePoint logo;
    private Obstacles name;
    private Rectangle slide;
    private final double minimumFrequency = 0.002;
    private final double maxFrequency = 0.04;
    private final double maxBSize = 1220;
    private int width = 50;

    public SliderObstacle(Obstacles type, Point topLeft) {
        this.name = type;

        this.logo = new ImagePoint(String.format("res/Obstacles/%s.png", type), topLeft);
        if (type == Obstacles.ROCK) {
            logo.setScale(0.1);
        }
        logo.setPos(topLeft.x - logo.getWidth()*logo.getScale()/2, topLeft.y);
        this.slide = new Rectangle(topLeft, maxBSize, width);
    }

    public void draw() {
        Point topLeft = new Point(logo.getPos().x + logo.getWidth()*logo.getScale()/2, logo.getPos().y);
        double currentBar;
        double currentFrequency;
        currentFrequency = obstaclesSettingsSingleton.getFrequency(name);
        currentBar = (currentFrequency - minimumFrequency)/(maxFrequency - minimumFrequency) * maxBSize;

        if (gameSettingsSingleton.getObstaclesSettingsSingleton().isObstacle(name)) {
            Drawing.drawRectangle(topLeft.x, topLeft.y + logo.getHeight()*logo.getScale()/4, maxBSize, 50, new Colour(0, 0, 0, 0.3));
            Drawing.drawRectangle(topLeft.x, topLeft.y + logo.getHeight()*logo.getScale()/4, currentBar, 50,
                    new Colour( (1 - (maxBSize - currentBar)/maxBSize)*25.5/100 + 106.7/255d,
                            (1 - (maxBSize - currentBar)/maxBSize)*25.5/100 + 143.9/255d,
                            (1 - (maxBSize - currentBar)/maxBSize)*25.5/100 + 121.6/255d));
            drawSliderIndicator(logo.getHeight()*logo.getScale()*1.1, currentBar + topLeft.x, topLeft.y - logo.getHeight()*logo.getScale()/2);
        }
        logo.draw();
    }

    public void interact(Input input) {
        double mouseX = input.getMouseX();
        Point topLeft = logo.getPos();
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
            if (logo.getRectangle().intersects(input.getMousePosition())) {
                obstaclesSettingsSingleton.toggle(name);
            }
        }
    }

    public void setPos(double x, double y) {
        this.logo.setPos(new Point(x, y));
    }

    private void drawSliderIndicator(double size, double x, double y) {
        Drawing.drawRectangle(x,y + size/2, 11, size + 10, new Colour(0.7,0.7,0.7,1));
        Drawing.drawRectangle(x + 2.75,y + size/2 + 4, 5, size, new Colour(0.2,0.2,0.2,1));
    }

}
