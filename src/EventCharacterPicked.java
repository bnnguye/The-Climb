import java.util.ArrayList;

public class EventCharacterPicked extends EventInterface {

    private ImagePointManagerSingleton imagePointManagerSingleton = ImagePointManagerSingleton.getInstance();

    ArrayList<ImagePoint> characterRenders;

    private String currentCharacterPicked;
    ImagePoint charImage;
    private boolean canInteract = true;

    public EventCharacterPicked(int frames, String event) {
        int currentTime = TimeLogger.getInstance().getFrames();
        this.frames = frames + currentTime;
        this.event = event;
        currentCharacterPicked = event.split(" ")[event.split(" ").length - 1];
    }

    public void process() {
        getAllCharacterImages();

        int middleCharacterIndex = characterRenders.size() % 2 == 1 ?
                (characterRenders.size() / 2) + 1 : characterRenders.size() / 2;

        charImage = imagePointManagerSingleton.getImages().get(middleCharacterIndex);
        int currentTime = TimeLogger.getInstance().getFrames();
        canInteract = false;
        if ((frames - currentTime) % 2 == 0) {
            charImage.setFlashing(true);
        }
        else if ((frames - currentTime) % 1 == 0) {
            charImage.setFlashing(false);
        }
        if (frames - TimeLogger.getInstance().getFrames() == 0) {
            charImage.setFlashing(false);
            canInteract = true;
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

    private void getAllCharacterImages() {
        ArrayList<ImagePoint> characterDisplays = new ArrayList<>();
        for (ImagePoint image: imagePointManagerSingleton.getImages()) {
            if ("CharacterRender".equals(image.getTag())) {
                characterDisplays.add(image);
            }
        }
        this.characterRenders = characterDisplays;
    }

}