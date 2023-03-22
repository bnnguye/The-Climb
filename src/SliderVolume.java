import bagel.Drawing;
import bagel.Image;
import bagel.Input;
import bagel.MouseButtons;
import bagel.util.Colour;
import bagel.util.Point;

public class SliderVolume extends Slider {

    MusicPlayer musicPlayer = MusicPlayer.getInstance();

    private final String name;
    private final String type = "Volume";
    private Point topLeft;
    private final double maxBSize = 1220;

    public SliderVolume(String name, Point pos) {
        this.name = name;
        this.topLeft = pos;
    }

    public String getName() {
        return name;
    }

    public void draw() {
        double currentFrequency;
        double minimumFrequency = 0;
        double maxFrequency;
        if (name.equals("mainVolume")) {
            currentFrequency = musicPlayer.getMainVolume();
            maxFrequency = musicPlayer.getMaxMainVol();
        }
        else {
            currentFrequency = musicPlayer.getEffectVolume();
            maxFrequency = musicPlayer.getMaxEffectVol();
        }
        double currentBar = (currentFrequency - minimumFrequency)/(maxFrequency - minimumFrequency) * maxBSize;

        Image sliderIndicator = new Image("res/misc/sliderIndicatorS.png");
        double width = 50;

        Drawing.drawRectangle(topLeft, maxBSize, width, new Colour(0, 0, 0, 0.5));
        Drawing.drawRectangle(topLeft, currentBar, width, new Colour((1 - currentBar/maxBSize), 1, currentBar/maxBSize, currentBar/maxBSize));
        sliderIndicator.drawFromTopLeft(currentBar + topLeft.x - sliderIndicator.getWidth()/2, topLeft.y - 10);
        new FontSize(Fonts.DEJAVUSANS, width).draw(name, topLeft.x, topLeft.y);
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
