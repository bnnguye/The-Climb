public class EventShakeMap extends EventInterface {

    private boolean alternate = false;

    public EventShakeMap (int duration) {
        this.frames = duration + TimeLogger.getInstance().getFrames();
    }

    public void process() {
        if (GameSettingsSingleton.getInstance().getMap() != null) {
            GameSettingsSingleton.getInstance().getMap().shake(alternate? 20: -20);
            alternate = !alternate;
        }
    }
}
