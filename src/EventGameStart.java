public class EventGameStart extends  EventInterface {

    private boolean init = false;

    public EventGameStart(int frames, String event) {
        this.frames = frames + TimeLogger.getInstance().getFrames();
        this.event = event;
    }

    public void process() {
        int refreshRate = SettingsSingleton.getInstance().getRefreshRate();
        int currentTime = TimeLogger.getInstance().getFrames();
        FontSize countDownFont = new FontSize(Fonts.STORYTIME, 100);
        Map map = GameSettingsSingleton.getInstance().getMap();
        canInteract = false;

        if (!init) {
            init = true;
            map.goToSummit();
        }
        else if (frames - currentTime < 5 * refreshRate) {
            map.descend();
        }
        if (frames - currentTime == 1 * refreshRate) {
            MusicPlayer.getInstance().addMusic("music/Start.wav");
        }
    }
}
