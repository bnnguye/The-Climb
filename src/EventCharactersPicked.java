public class EventCharactersPicked extends EventInterface {

    private boolean cannotInteract = false;

    public EventCharactersPicked(String event) {
        this.event = event;
        this.frames = SettingsSingleton.getInstance().getRefreshRate() + TimeLogger.getInstance().getFrames();
    }

    public void process() {
        if (frames - TimeLogger.getInstance().getFrames() == 1) {
            SettingsSingleton.getInstance().setGameState(4);
        }
    }
}
