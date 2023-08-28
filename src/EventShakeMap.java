public class EventShakeMap extends EventInterface {

    private boolean alternate = false;

    private int offset = 50;
    private int offsetTotal = 0;

    private int duration;

    public EventShakeMap (int duration) {
        this.event = "Event Shake Map";
        this.frames = duration + TimeLogger.getInstance().getTime();
        this.duration = duration;
        if (GameSettingsSingleton.getInstance().getMap() != null) {
            GameSettingsSingleton.getInstance().getMap().shake(50);
            offsetTotal = offset/2;
        }
    }

    public void process() {
        int currentTime = TimeLogger.getInstance().getTime();
        Map map = GameSettingsSingleton.getInstance().getMap();
        canInteract = frames - currentTime <= duration / 2;

        int offset = alternate? this.offset:-1 * this.offset;

        if (GameSettingsSingleton.getInstance().getMap() != null) {
            if (currentTime % 16 == 0) {
                offsetTotal += offset;
                alternate = !alternate;
                map.shake(offset);

            }
            else if (currentTime % 8 == 0) {
                offsetTotal += offset;
                alternate = !alternate;
                map.shake(offset);
            }
        }

        if (frames == currentTime + 1) {
            map.shake(-offsetTotal);
        }
    }
}
