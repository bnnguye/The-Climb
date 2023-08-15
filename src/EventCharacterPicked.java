import bagel.util.Point;

import java.util.ArrayList;

public class EventCharacterPicked extends EventInterface {

    ArrayList<ImagePoint> characterRenders;

    ImagePoint charImage;
    Point originalPosition = null;

    int duration;

    public EventCharacterPicked(int frames, String event) {
        duration = frames;
        int currentTime = TimeLogger.getInstance().getFrames();
        this.frames = frames + currentTime;
        this.event = event;
        this.canInteract = false;
    }

    public void process() {
        getAllCharacterImages();

        int middleCharacterIndex = characterRenders.size() % 2 == 1 ?
                (characterRenders.size() / 2) + 1 : characterRenders.size() / 2;

        charImage = imagePointManagerSingleton.getImages().get(middleCharacterIndex);

        if (originalPosition == null) {
            originalPosition = charImage.getPos();
            charImage.setPos(-charImage.getPos().x, charImage.getPos().y);
        }
        if (charImage.getPos().distanceTo(originalPosition) > 101 && charImage.getPos().x < originalPosition.x) {
            charImage.move(50, 0);
        }
        else {
            charImage.move(0.1,0);
        }

        int currentTime = TimeLogger.getInstance().getFrames();
        int refreshRate = TimeLogger.getInstance().getRefreshRate();
//        if (duration - (frames - currentTime) < 2*refreshRate) {
//            if ((frames - currentTime) % 8 == 0) {
//                charImage.setFlashing(true);
//            }
//            else if ((frames - currentTime) % 4 == 0) {
//                charImage.setFlashing(false);
//            }
//        }

        if (frames - TimeLogger.getInstance().getFrames() == 1) {
            charImage.setFlashing(false);
            charImage.setOpacity(0.3);
            charImage.setPos(originalPosition);
        }
    }

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
