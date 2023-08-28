import bagel.Window;
import bagel.util.Point;

public class EventGameStateZero extends EventInterface {

    ImagePointManagerSingleton imagePointManagerSingleton = ImagePointManagerSingleton.getInstance();

    private int currentTime = 0;

    public EventGameStateZero() {
        this.event = "EventGameStateZero";
        this.frames = 40 + TimeLogger.getInstance().getTime();
        imagePointManagerSingleton.add(new ImagePoint("res/menu/main/leftcover.png", new Point(0,5000)));
        imagePointManagerSingleton.add(new ImagePoint("res/menu/main/rightcover.png", new Point(0,5000)));
    }

    public void process() {
        ImagePoint leftCover = imagePointManagerSingleton.get("res/menu/main/leftcover.png");
        ImagePoint rightCover = imagePointManagerSingleton.get("res/menu/main/rightcover.png");

        if (currentTime == 0) {
            leftCover.setPos(-leftCover.getWidth(), 0);
            rightCover.setPos(Window.getWidth(), 0);
        }

        if (leftCover != null) {
            leftCover.setPos(leftCover.getPos().x + (leftCover.getWidth())/41, 0);
        }
        if (rightCover != null) {
            rightCover.setPos(rightCover.getPos().x - (rightCover.getWidth())/41, 0);
        }

        if (currentTime + 1 == frames) {
            if (leftCover != null) {
                leftCover.setPos(0,0);
            }
            if (rightCover != null) {
                rightCover.setPos(0,0);
            }
        }

        currentTime++;
    }
}
