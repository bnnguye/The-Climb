public class EventCharactersPicked extends EventInterface {

    public EventCharactersPicked(String event) {
        this.event = event;
        this.frames = TimeLogger.getInstance().getRefreshRate() + TimeLogger.getInstance().getTime();
    }

    public void process() {
        if (frames - TimeLogger.getInstance().getTime() == 1) {
            SettingsSingleton.getInstance().setGameState(4);
        }
    }
}
