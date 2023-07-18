import bagel.util.Point;

public class EventGameMode extends EventInterface {

    ImagePointManagerSingleton imagePointManagerSingleton = ImagePointManagerSingleton.getInstance();

    int duration;

    public EventGameMode() {
        duration = SettingsSingleton.getInstance().getRefreshRate()/2;
        this.frames = TimeLogger.getInstance().getFrames() + duration;

        if (imagePointManagerSingleton.get("res/menu/main/leftcover.png") == null) {
            imagePointManagerSingleton.add(new ImagePoint("res/menu/main/leftcover.png", new Point(0,0)));
            imagePointManagerSingleton.add(new ImagePoint("res/menu/main/rightcover.png", new Point(0,0)));
        }
    }

    public void process() {
        int currentTime = TimeLogger.getInstance().getFrames();
        int offset = frames - currentTime <= duration/2? 50: -50;


        ImagePoint leftCover = imagePointManagerSingleton.get("res/menu/main/leftcover.png");
        ImagePoint rightCover = imagePointManagerSingleton.get("res/menu/main/rightcover.png");

        if (leftCover != null) {
            System.out.println("left");
            leftCover.move(offset, 0);
        }
        if (rightCover != null) {
            System.out.println("right");
            rightCover.move(-offset, 0);
        }

    }
}
