import bagel.Window;
import bagel.util.Point;

public class EventFrieza extends EventInterface {

    ImagePoint render = new ImagePoint("res/maps/mapcharacters/frieza/render.png", new Point(0, Window.getHeight()));
    ImagePoint ball = new ImagePoint("res/maps/mapcharacters/frieza/ball.png", new Point(0, Window.getHeight()));

    public EventFrieza() {
        canInteract = true;
        frames = 14 * refreshRate + timeLogger.getTime();
        musicPlayer.addMusic("music/mapevents/frieza/sound.wav");
        imagePointManagerSingleton.add(render);
        imagePointManagerSingleton.add(ball);
    }

    public void process() {
        canInteract = true;

        if (timePassed == 9 * refreshRate) {
            musicPlayer.addMusic("music/mapevents/frieza/activate.wav");
            ball.setPos(Window.getWidth()/2 - ball.getWidth()/2, -ball.getHeight());
        }
        if (timePassed >= 9 * refreshRate && timePassed < 11 * refreshRate) {
            canInteract = false;
            render.setPos(0,0);
        }
        else if (timePassed > 11 * refreshRate) {
            updateball();
        }
        if (timePassed == 11 * refreshRate) {
            imagePointManagerSingleton.remove(render);
        }

        if (timePassed + 1 == frames) {
            imagePointManagerSingleton.remove(ball);
            imagePointManagerSingleton.remove(render);
        }
        timePassed++;
    }

    public void updateball() {
        ball.move(0, 12);
        for (Player player: settingsSingleton.getPlayers()) {
            if (player.getCharacter().getRectangle().intersects(ball.getRectangle())) {
                player.getCharacter().setLives(0);
            }
        }
    }
}
