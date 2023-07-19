import java.util.ArrayList;

public class EventSideCharactersPicked extends EventInterface {

    private ImagePointManagerSingleton imagePointManagerSingleton = ImagePointManagerSingleton.getInstance();

    public EventSideCharactersPicked(String event) {
        int currentTime = TimeLogger.getInstance().getFrames();
        this.frames = currentTime + SettingsSingleton.getInstance().getRefreshRate();
        this.event = event;
    }

    public void process() {
        if (frames - TimeLogger.getInstance().getFrames() == 1) {
            SettingsSingleton.getInstance().setGameState(5);
        }
    }
}
