import bagel.Window;
import bagel.util.Point;

import java.util.ArrayList;
import java.util.Arrays;

public class EventDio extends EventInterface {
    // places a marker for each player in which 1 second after he ora oras

    ImagePoint render = new ImagePoint("res/maps/mapcharacters/dio/render.png", new Point(0,-Window.getHeight()));
    ImagePoint screen = new ImagePoint("res/misc/black.png", new Point(0,0));
    ImagePoint roller = new ImagePoint("res/maps/mapcharacters/dio/roller.png", new Point(0,-Window.getHeight()));

    ArrayList<ImagePoint> indicators = new ArrayList<>();



    public EventDio() {
        this.frames = 12 * refreshRate + timeLogger.getTime();
        musicPlayer.addMusic("music/mapEvents/Dio/dio enters.wav");
        imagePointManagerSingleton.add(screen);
        imagePointManagerSingleton.add(render);
        imagePointManagerSingleton.add(roller);
        roller.setPos(Window.getWidth()/2d - roller.getWidth()/2d, -roller.getHeight());
        canInteract = true;
        screen.setOpacity(0.25);
            ImagePoint indicator1 = new ImagePoint("res/arrows/indicator.png", new Point(roller.getPos().x, 50));
            ImagePoint indicator2 = new ImagePoint("res/arrows/indicator.png", new Point(roller.getPos().x + 200, 50));
            ImagePoint indicator3 = new ImagePoint("res/arrows/indicator.png", new Point(roller.getPos().x + 400, 50));
            ImagePoint indicator4 = new ImagePoint("res/arrows/indicator.png", new Point(roller.getPos().x + 600, 50));
            ImagePoint indicator5 = new ImagePoint("res/arrows/indicator.png", new Point(roller.getPos().x + 800, 50));


        indicators.addAll(Arrays.asList(indicator1, indicator2, indicator3, indicator4, indicator5));
    }

    public void process() {

        if (timePassed == 4 * refreshRate) {
            imagePointManagerSingleton.addAll(indicators);
        }

        if (timePassed >= 6 * refreshRate) {
            if (timePassed < 11 * refreshRate) {
                if (timePassed % 16 == 0) {
                    toggleIndicators(true);
                }
                else if (timePassed % 8 == 0) {
                    toggleIndicators(false);
                }
            }
        }

        if (timePassed == 8 * refreshRate) {
            musicPlayer.addMusic("music/mapEvents/Dio/dio.wav");
            imagePointManagerSingleton.removeAll(indicators);

        }
        if (timePassed >= 8 * refreshRate) {
            dio = true;
            render.setPos(0,0);
            if (timePassed >= 10 * refreshRate) {
                render.setPos(Window.getWidth(), 0);
            }

        }

        if (timePassed >= 11 * refreshRate) {
            moveRoller();
            for (Player player: settingsSingleton.getPlayers()) {
                if (player.getCharacter().getRectangle().intersects(roller.getRectangle())) {
                    player.getCharacter().reduceLive();
                }
            }
        }

        if (timePassed + 1 == frames) {
            imagePointManagerSingleton.remove(render);
            imagePointManagerSingleton.remove(roller);
            imagePointManagerSingleton.remove(screen);
        }

        timePassed++;
    }

    public void moveRoller() {
        roller.move(0, (Window.getHeight() + roller.getHeight())/(double) refreshRate);
    }

    public void toggleIndicators(boolean bool) {
        for (ImagePoint indicator: indicators) {
            indicator.setFlashing(bool);
        }
    }
}
