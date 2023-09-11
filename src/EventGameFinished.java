import bagel.Window;
import bagel.util.Point;

public class EventGameFinished extends EventInterface {

    private final int totalDuration;
    private int currentTime = 0;

    public EventGameFinished() {
        this.event = "EventGameFinished";
        this.canInteract = false;
        this.totalDuration = 3;
        this.frames = totalDuration * TimeLogger.getInstance().getRefreshRate() + timeLogger.getTime();
        imagePointManagerSingleton.add(new ImagePoint("res/misc/black.png", new Point(0, -Window.getHeight())));
        imagePointManagerSingleton.get("res/misc/black.png").setOpacity(0);
    }

    public void process() {

        if (currentTime == 2 * refreshRate) {
            if (settingsSingleton.getWinner() != null) {
                musicPlayer.addMusic(settingsSingleton.getWinner().getCharacter().playLine());
            }
        }

        if (currentTime < 2 * refreshRate) {
            double yoffset = Window.getHeight()/ (2.0 * refreshRate);
            imagePointManagerSingleton.get("res/misc/black.png").move(0,yoffset);
            imagePointManagerSingleton.get("res/misc/black.png").
                    setOpacity((double) currentTime / (double) (3 * refreshRate));
        }

        if (currentTime + 1 == totalDuration * refreshRate) {

            int index = 0;
            for (Player player: settingsSingleton.getPlayers()) {
                ImagePoint characterRender = new ImagePoint(String.format("res/characters/%s/Render.png",
                        player.getCharacter().getFullName()), new Point(0,0), "characterRender");
                characterRender.setPos(1000 + 2000 * index, Window.getHeight() - characterRender.getHeight());
                imagePointManagerSingleton.add(characterRender);
                index++;
            }

            settingsSingleton.setGameStateString("Game Finished");
            settingsSingleton.setGameState(7);
            eventsToBeAdded.add(new EventGameMode());
        }
        currentTime++;
    }
}
