import bagel.Window;
import bagel.util.Point;

public class EventDio extends EventInterface {
    // places a marker for each player in which 1 second after he ora oras

    ImagePoint render = new ImagePoint("res/maps/mapcharacters/dio/render.png", new Point(0,-Window.getHeight()));
    ImagePoint screen = new ImagePoint("res/misc/black.png", new Point(0,0));
    ImagePoint roller = new ImagePoint("res/maps/mapcharacters/dio/roller.png", new Point(0,-Window.getHeight()));



    public EventDio() {
        this.frames = 12 * refreshRate + timeLogger.getTime();
        musicPlayer.addMusic("music/mapEvents/Dio/dio enters.wav");
        imagePointManagerSingleton.add(screen);
        imagePointManagerSingleton.add(render);
        imagePointManagerSingleton.add(roller);
        roller.setPos(Window.getWidth()/2d - roller.getWidth()/2d, -roller.getHeight());
        canInteract = true;
        screen.setOpacity(0.25);
    }

    public void process() {

        if (timePassed == 8 * refreshRate) {
            musicPlayer.addMusic("music/mapEvents/Dio/dio.wav");
        }
        if (timePassed >= 8 * refreshRate) {
            dio = true;
            render.setPos(0,0);
            if (timePassed >= 10 * refreshRate) {
                render.setPos(Window.getWidth(), 0);
            }
            else {
                // change opacity of screen
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
}
