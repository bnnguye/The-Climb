import bagel.Window;

public class EventGameStart extends  EventInterface {

    int refreshRate = SettingsSingleton.getInstance().getRefreshRate();

    private boolean init = false;

    public EventGameStart() {
        this.frames = (5 * refreshRate) + TimeLogger.getInstance().getFrames();
    }

    public void process() {
        int currentTime = TimeLogger.getInstance().getFrames();
        FontSize countDownFont = new FontSize(Fonts.STORYTIME, 250);
        Map map = GameSettingsSingleton.getInstance().getMap();
        canInteract = false;

        if (!init) {
            init = true;
            map.goToSummit();
        }
        else if (frames - currentTime < 4 * refreshRate) {
            if (map.currentHeight > 0) {
                map.descend();
                this.frames++;
            }
        }
        if (frames - currentTime == 3 * refreshRate) {
            MusicPlayer.getInstance().addMusic("music/Start.wav");
        }
        if (frames - currentTime <= 3 * refreshRate) {
            countDownFont.draw(String.format("%d", (frames - currentTime)/refreshRate + 1), Window.getWidth()/2, Window.getHeight()/2);
        }
    }
}
