public class EventGameFinished extends EventInterface {

    private ImagePointManagerSingleton imagePointManagerSingleton = ImagePointManagerSingleton.getInstance();

    private boolean canInteract = false;

    public EventGameFinished(String event) {
        int currentTime = TimeLogger.getInstance().getFrames();
        this.frames = 4 * SettingsSingleton.getInstance().getRefreshRate() + currentTime;
    }

    public void process() {

    }
}
