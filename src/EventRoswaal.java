public class EventRoswaal extends EventInterface {

    public EventRoswaal() {
        canInteract = true;
        this.frames = 4 * timeLogger.getRefreshRate() + timeLogger.getTime();
        musicPlayer.addMusic("music/mapEvents/roswaal/activate.wav");
    }

    public void process() {
        if (timePassed == 1 * refreshRate) {
            reverse();
        }

        if (timePassed + 1 == frames) {
            reverse();
        }

        timePassed++;
    }

    public void reverse() {
        for (Player player: settingsSingleton.getPlayers()) {
            player.reverseControls();
        }

        GameSettingsSingleton.getInstance().getMap().reverse();
    }
}
