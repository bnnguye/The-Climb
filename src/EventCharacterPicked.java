public class EventCharacterPicked extends EventInterface {

    private ImagePointManagerSingleton imagePointManagerSingleton = ImagePointManagerSingleton.getInstance();

    private String currentCharacterPicked;
    ImagePoint charImage;
    private boolean canInteract = true;

    public EventCharacterPicked(int frames, String event) {
        int currentTime = TimeLogger.getInstance().getFrames();
        this.frames = frames + currentTime;
        this.event = event;
        currentCharacterPicked = event.split(" ")[event.split(" ").length - 1];
        charImage = imagePointManagerSingleton.get(String.format("res/renders/Characters/%s.png", currentCharacterPicked));
    }

    public void process() {
        int currentTime = TimeLogger.getInstance().getFrames();
        canInteract = false;
        if (frames - currentTime > 2) {
            if (frames - currentTime % 10 == 0) {
                charImage.setDarken(true);
            }
            else if (frames - currentTime % 5 == 0) {
                charImage.setDarken(false);
            }
        }
        if (frames < TimeLogger.getInstance().getFrames()) {
            charImage.setDarken(false);
            canInteract = false;
        }
    }

    @Override
    public boolean isCanInteract() {
        return canInteract;
    }

    @Override
    public String getEvent() {
        return event;
    }

    @Override
    public int getFrames() { return frames;}
}
