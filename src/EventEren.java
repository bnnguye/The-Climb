import bagel.Window;
import bagel.util.Point;

public class EventEren extends EventInterface {

    private final GameEntities gameEntities = GameEntities.getInstance();

    ImagePoint render = new ImagePoint("res/maps/mapcharacters/eren/render.png", new Point(0, -Window.getHeight()));


    public EventEren() {
        canInteract = true;
        musicPlayer.addMusic("music/mapEvents/Eren/eren.wav");
        frames = timeLogger.getTime() + 22 * refreshRate;
        imagePointManagerSingleton.add(render);
    }


    public void process() {
        canInteract = true;
        expandRocks();


        if (timePassed >= 11 * refreshRate && timePassed < 14 * refreshRate) {
            canInteract = false;
            if (timePassed < (int) (11.5 * refreshRate)) {
                render.move(0, Window.getHeight()/(double) (refreshRate/2));
            }
            else if (timePassed == (int) (12.5 * refreshRate)) {
                imagePointManagerSingleton.remove(render);

            }
        }

        if (timePassed == 15 * refreshRate) {
            spawnRocks();
        }

        timePassed++;
    }


    public void spawnRocks() {
        int spacing = Window.getWidth()/6;

        for (int i = 0; i < 6; i ++) {
            gameEntities.getObstacles().add(new ObstacleRock(new Point(spacing * i, -200)));
        }
    }

    public void expandRocks() {
        for (Obstacle obstacle: gameEntities.getObstacles()) {
            if (ObstacleRock.class == obstacle.getClass()) {
                ((ObstacleRock) obstacle).increaseScale(0.0002);
            }
        }
    }
}
