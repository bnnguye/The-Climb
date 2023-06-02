import java.util.ArrayList;

public class EventSideCharactersPicked extends EventInterface {

    private ImagePointManagerSingleton imagePointManagerSingleton = ImagePointManagerSingleton.getInstance();

    private boolean canInteract = false;

    public EventSideCharactersPicked(int frames, String event) {
        int currentTime = TimeLogger.getInstance().getFrames();
        this.frames = frames + currentTime;
        this.event = event;
    }

    public void process() {
        if (frames - TimeLogger.getInstance().getFrames() == 1) {
            SettingsSingleton.getInstance().setGameState(5);
        }
    }
}
