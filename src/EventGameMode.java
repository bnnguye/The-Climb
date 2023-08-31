import bagel.util.Point;

public class EventGameMode extends EventInterface {

    ImagePointManagerSingleton imagePointManagerSingleton = ImagePointManagerSingleton.getInstance();

    int duration;

    public EventGameMode() {
        duration = TimeLogger.getInstance().getRefreshRate()/2;
        this.frames = TimeLogger.getInstance().getTime() + duration;

        if (imagePointManagerSingleton.get("res/menu/main/leftcover.png") == null) {
            imagePointManagerSingleton.add(new ImagePoint("res/menu/main/leftcover.png", new Point(0,0)));
            imagePointManagerSingleton.add(new ImagePoint("res/menu/main/rightcover.png", new Point(0,0)));
        }
    }

    public void process() {
        int currentTime = TimeLogger.getInstance().getTime();
        int offset = frames - currentTime <= duration/2? 50: -50;


        ImagePoint leftCover = imagePointManagerSingleton.get("res/menu/main/leftcover.png");
        ImagePoint rightCover = imagePointManagerSingleton.get("res/menu/main/rightcover.png");

        if (leftCover != null) {
            leftCover.move(offset, 0);
        }
        if (rightCover != null) {
            rightCover.move(-offset, 0);
        }

    }
}
