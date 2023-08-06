import bagel.Drawing;
import bagel.Window;
import bagel.util.Colour;
import bagel.util.Point;

public class EventGameFinished extends EventInterface {

    ImagePointManagerSingleton imagePointManagerSingleton = ImagePointManagerSingleton.getInstance();

    private final int duration;

    public EventGameFinished() {
        this.event = "EventGameFinished";
        this.canInteract = false;
        this.duration = 3;
        int currentTime = TimeLogger.getInstance().getFrames();
        this.frames = duration * TimeLogger.getInstance().getRefreshRate() + currentTime;
        imagePointManagerSingleton.add(new ImagePoint("res/misc/black.png", new Point(Window.getWidth(),0)));
        imagePointManagerSingleton.add(new ImagePoint("res/misc/white.png", new Point(0,0)));
        imagePointManagerSingleton.get("res/misc/white.png").setOpacity(0);
    }

    public void process() {
        int currentTime = TimeLogger.getInstance().getFrames();

        if (frames - currentTime > TimeLogger.getInstance().getRefreshRate()) {
            int xoffset = -Window.getWidth()/((duration - 1) * TimeLogger.getInstance().getRefreshRate());
            System.out.println(xoffset);
            imagePointManagerSingleton.get("res/misc/black.png").move(xoffset,0);
        }
        else if (frames - currentTime <= TimeLogger.getInstance().getRefreshRate()) {

        }

        if (frames - currentTime == 0) {
            SettingsSingleton.getInstance().setGameState(7);
        }
    }
}
