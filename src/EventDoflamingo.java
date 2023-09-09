import bagel.Window;
import bagel.util.Point;

import java.util.ArrayList;

public class EventDoflamingo extends EventInterface {

    private ArrayList<ImagePoint> rectangles = new ArrayList<>();
    private ArrayList<ImagePoint> indicators = new ArrayList<>();

    public EventDoflamingo() {
        this.frames = 6 * refreshRate + timeLogger.getTime();
        musicPlayer.addMusic("music/mapEvents/Doflamingo/doflamingo.wav");
    }

    public void process() {
        if (timePassed <= 2 * refreshRate) {
            new ImagePoint("res/maps/mapcharacters/doflamingo/render.png", new Point(0,0)).draw();
        }
        if (timePassed > 2 * refreshRate) {
            canInteract = true;
            if (timePassed % 72 == 0){
                createRectangle();
            }
        }
        updateRectangles();

        if (timePassed + 1 == frames) {
            imagePointManagerSingleton.removeAll(rectangles);
            rectangles.clear();
        }
        timePassed++;
    }

    public void createRectangle() {
        musicPlayer.addMusic("music/mapEvents/Doflamingo/string.wav");
        ImagePoint newString = new ImagePoint("res/maps/mapcharacters/doflamingo/string.png", new Point(Math.random() * Window.getWidth(), -3 * Window.getHeight()));
        ImagePoint newIndicator = new ImagePoint("res/arrows/indicator.png", new Point(newString.getPos().x, 50));
        indicators.add(newIndicator);
        rectangles.add(newString);
        imagePointManagerSingleton.add(newString);
        imagePointManagerSingleton.add(newIndicator);
    }

    public void updateRectangles() {
        ArrayList<ImagePoint> rectanglesToRemove = new ArrayList<>();
        for (ImagePoint rectangle: rectangles) {
            rectangle.move(0, 16);
            for (Player player: settingsSingleton.getPlayers()) {
                if (player.getCharacter().getLives() > 0 && player.getCharacter().getRectangle().intersects(rectangle.getRectangle())) {
                    player.getCharacter().reduceLive();
                    rectanglesToRemove.add(rectangle);
                    break;
                }
            }
            if (rectangle.getPos().y > Window.getHeight()) {
                rectanglesToRemove.add(rectangle);
                imagePointManagerSingleton.remove(rectangle);
                if (indicators.size() > 0) {
                    ImagePoint indicator = indicators.get(0);
                    indicators.remove(indicator);
                    imagePointManagerSingleton.remove(indicator);
                }
            }
        }
        rectangles.removeAll(rectanglesToRemove);
        imagePointManagerSingleton.removeAll(rectanglesToRemove);
        for (int i = 0; i < rectanglesToRemove.size(); i++) {
            if (indicators.size() > 0) {
                imagePointManagerSingleton.remove(indicators.get(0));
                indicators.remove(0);
            }
        }
    }
}
