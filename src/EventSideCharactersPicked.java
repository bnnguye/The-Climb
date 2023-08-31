public class EventSideCharactersPicked extends EventInterface {

    private ImagePointManagerSingleton imagePointManagerSingleton = ImagePointManagerSingleton.getInstance();

    public EventSideCharactersPicked(String event) {
        int currentTime = TimeLogger.getInstance().getTime();
        this.frames = currentTime + TimeLogger.getInstance().getRefreshRate();
        this.event = event;
    }

    public void process() {
        if (frames - TimeLogger.getInstance().getTime() == 1) {
            SettingsSingleton.getInstance().setGameState(5);
        }
    }
}
