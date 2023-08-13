import bagel.Window;
import bagel.util.Point;

public class EventGameFinished extends EventInterface {

    ImagePointManagerSingleton imagePointManagerSingleton = ImagePointManagerSingleton.getInstance();
    SettingsSingleton settingsSingleton = SettingsSingleton.getInstance();
    EventsListenerSingleton eventsListenerSingleton = EventsListenerSingleton.getInstance();
    TimeLogger timeLogger = TimeLogger.getInstance();
    int refreshRate = timeLogger.getRefreshRate();

    private final int totalDuration;
    private int currentTime = 0;

    public EventGameFinished() {
        this.event = "EventGameFinished";
        this.canInteract = false;
        this.totalDuration = 3;
        this.frames = totalDuration * TimeLogger.getInstance().getRefreshRate() + timeLogger.getFrames();
        imagePointManagerSingleton.add(new ImagePoint("res/misc/black.png", new Point(0, -Window.getHeight())));
        imagePointManagerSingleton.get("res/misc/black.png").setOpacity(0);
    }

    public void process() {

        if (currentTime == 2 * refreshRate) {
            if (settingsSingleton.getWinner() != null) {
                String winner = settingsSingleton.getWinner().getCharacter().getFullName();
                imagePointManagerSingleton.add(new ImagePoint(String.format("res/characters/%s/Render.png",
                        winner), new Point(0,0)));
                ImagePoint render = imagePointManagerSingleton.get(String.format("res/characters/%s/Render.png", winner));
                render.setPos(Window.getWidth()/2d - render.getWidth()/2, 0);
                render.setDarken(true);
                render.setOpacity(0.5);
            }
            eventsListenerSingleton.getEventsListener().addEvent(new EventGameStateZero());

            int index = 1;
            for (Player player: settingsSingleton.getPlayers()) {
                ImagePoint characterRender = new ImagePoint(String.format("res/characters/%s/Render.png",
                        player.getCharacter().getFullName()), new Point(1000 * index,24));
                imagePointManagerSingleton.add(characterRender);
                index++;
            }
            GameSettingsSingleton.getInstance().getMap().generateMap();
        }

        if (currentTime <= 2 * refreshRate) {
            double yoffset = Window.getHeight()/ (2.0 * refreshRate);
            imagePointManagerSingleton.get("res/misc/black.png").move(0,yoffset);
            imagePointManagerSingleton.get("res/misc/black.png").
                    setOpacity((double) currentTime / (double) (3 * refreshRate));
        }
        else if (currentTime <= 3 * refreshRate) {

        }

        if (currentTime + 1 == totalDuration * refreshRate) {
            settingsSingleton.setGameStateString("Game Finished");
            settingsSingleton.setGameState(7);
        }
        currentTime++;
    }
}
