import bagel.*;
import bagel.util.Colour;
import bagel.util.Point;
import bagel.util.Rectangle;

public class SliderVolume extends Slider {

    MusicPlayer musicPlayer = MusicPlayer.getInstance();

    private final String name;
    private final String type = "Volume";
    private Point topLeft;
    private final Rectangle slide;
    private final double maxBSize = 1220;
    private int width = 50;

    public SliderVolume(String name, Point pos) {
        this.name = name;
        this.topLeft = pos;
        this.slide = new Rectangle(topLeft, maxBSize, width);
    }

    public String getName() {
        return name;
    }

    public void draw() {
        double currentBar = (currentFrequency() - minimumFrequency())/(maxFrequency() - minimumFrequency()) * maxBSize;

        Image sliderIndicator = new Image("res/misc/sliderIndicatorS.png");

        Drawing.drawRectangle(topLeft, maxBSize, width, new Colour(0, 0, 0, 0.5));
        Drawing.drawRectangle(topLeft, currentBar, width, new Colour((1 - currentBar/maxBSize), 1, currentBar/maxBSize, currentBar/maxBSize));
        sliderIndicator.drawFromTopLeft(currentBar + topLeft.x - sliderIndicator.getWidth()/2, topLeft.y - 10);
        new FontSize(Fonts.GEOMATRIX, width).draw(name, topLeft.x, topLeft.y - 10, new DrawOptions().setBlendColour(Colour.BLACK));
    }

    public void interact(Input input) {
        double mouseX = 0;
        if (input != null) {
            mouseX = input.getMouseX();
        }
        if (input != null && input.isDown(MouseButtons.LEFT)) {
            if (slide.intersects(input.getMousePosition())) {
                if (mouseX > topLeft.x + maxBSize) {
                    mouseX = topLeft.x + maxBSize;
                }
                double newFrequency = ((mouseX - topLeft.x)/maxBSize)*(maxFrequency() - minimumFrequency()) + minimumFrequency();
                if (newFrequency > maxFrequency()) {
                    newFrequency = maxFrequency();
                }
                else if (newFrequency < minimumFrequency()) {
                    newFrequency = minimumFrequency();
                }
                else if ((int) newFrequency == 0) {
                    newFrequency = 0;
                }
                else if ((int) (newFrequency + 0.5) == maxFrequency()) {
                    newFrequency = maxFrequency();
                }
                if (name.equals("Main Volume")) {
                    musicPlayer.setMainVolume(newFrequency);
                }
                else {
                    musicPlayer.setEffectVolume(newFrequency);
                }
            }
        }
    }

    public void setPos(double x, double y) {
        this.topLeft = new Point(x, y);
    }

    private double currentFrequency() {
        if (name.equals("Main Volume")) {
            return musicPlayer.getMainVolume();
        }
        else {
            return musicPlayer.getEffectVolume();
        }
    }

    private double maxFrequency() {
        if (name.equals("Main Volume")) {
            return musicPlayer.getMaxMainVol();
        }
        else {
            return musicPlayer.getMaxEffectVol();
        }
    }

    private double minimumFrequency() {
        return 0;
    }
}
