import Enums.Obstacles;
import bagel.*;
import bagel.util.Colour;
import bagel.util.Point;
import bagel.util.Rectangle;

public class SliderObstacle extends Slider {

    private final ObstaclesSettingsSingleton obstaclesSettingsSingleton = ObstaclesSettingsSingleton.getInstance();
    private final GameSettingsSingleton gameSettingsSingleton = GameSettingsSingleton.getInstance();

    private final ImagePoint logo;
    private final Obstacles name;
    private final Rectangle slide;

    private final double minimumFrequency = 0.002;
    private final double maxFrequency = 0.04;
    private final double maxBSize = 1220;

    public SliderObstacle(Obstacles type, Point topLeft) {
        this.name = type;

        this.logo = new ImagePoint(String.format("res/Obstacles/%s.png", type), topLeft);
        if (type == Obstacles.ROCK) {
            logo.setScale(0.1);
        }
        this.slide = new Rectangle(topLeft, maxBSize, logo.getHeight() * logo.getScale());
    }

    public void draw() {
        Point topLeft = logo.getPos();
        double currentFrequency = obstaclesSettingsSingleton.getFrequency(name);
        double currentBar = (currentFrequency - minimumFrequency)/(maxFrequency - minimumFrequency) * maxBSize;
        Image sliderIndicator = new Image("res/misc/sliderIndicatorS.png");
        if (gameSettingsSingleton.getObstaclesSettingsSingleton().isObstacle(name)) {
            Drawing.drawRectangle(topLeft, maxBSize, logo.getHeight() * logo.getScale(), new Colour(0, 0, 0, 0.5));
            Drawing.drawRectangle(topLeft, currentBar, logo.getHeight() * logo.getScale(),
                    new Colour((1 - currentBar/maxBSize), currentBar/maxBSize, currentBar/(maxBSize*2), currentBar/maxBSize));
            sliderIndicator.drawFromTopLeft(currentBar + topLeft.x - sliderIndicator.getWidth()/2, topLeft.y - 10);
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


}
