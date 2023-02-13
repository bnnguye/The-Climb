public class EventGameStart extends  EventInterface {

    public EventGameStart(int frames, String event) {
        this.frames = frames + TimeLogger.getInstance().getFrames();
        this.event = event;
    }

    public void process() {
        FontSize countDownFont = new FontSize(Fonts.STORYTIME, 100);
        Map map = GameSettingsSingleton.getInstance().getMap();

    }
}
