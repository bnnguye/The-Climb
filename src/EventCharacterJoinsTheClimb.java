import bagel.util.Point;

public class EventCharacterJoinsTheClimb extends EventInterface {

    ImagePointManagerSingleton imagePointManager = ImagePointManagerSingleton.getInstance();

    public EventCharacterJoinsTheClimb(String character) {
        this.frames = 3 * TimeLogger.getInstance().getRefreshRate() + TimeLogger.getInstance().getFrames();
        imagePointManager.add(new ImagePoint("res/storycharacters/background.png", new Point(0,0), "background"));
        imagePointManager.add(new ImagePoint("res/storycharacters/joinstheclimb.png", new Point(0,0), "joinstheclimb"));
        imagePointManager.add(new ImagePoint("res/storycharacters/shader.png", new Point(0,0), "shader"));
        imagePointManager.add(new ImagePoint(String.format("res/storycharacters/%s.png", character), new Point(0,0), "character"));


    }

    public void process() {
    }
}
