public class EventRoswaal extends EventInterface {

    public EventRoswaal() {
        this.frames = 3 * timeLogger.getRefreshRate() + timeLogger.getTime();
        reverse();
    }

    public void process() {
        if (timeLogger.getTime() == frames - 1) {
            reverse();
        }
    }

    public void reverse() {
        for (Player player: settingsSingleton.getPlayers()) {
            player.reverseControls();
        }

        GameSettingsSingleton.getInstance().getMap().reverse();
    }
}
