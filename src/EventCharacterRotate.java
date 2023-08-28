import bagel.Window;

import java.util.ArrayList;

public class EventCharacterRotate extends EventInterface{

    ArrayList<ImagePoint> characterRenders;
    
    ImagePointManagerSingleton imagePointManagerSingleton = ImagePointManagerSingleton.getInstance();

    private final double minScale = 0.275;
    private final double maxScale = 1;
    private double calls;

    public EventCharacterRotate(String event) {
        int duration = TimeLogger.getInstance().getRefreshRate()/8;
        this.frames = duration + TimeLogger.getInstance().getTime();
        this.calls = duration;
        this.event = event;
    }

    public void process() {
        getAllCharacterImages();

        int currentTime = TimeLogger.getInstance().getTime();
        double spacing = Window.getHeight()/4d;
        double maxHeight = Window.getHeight()/8d;
        double minHeight = Window.getHeight()*3/4d;
        double sign = event.contains("LEFT") ? 1 : -1;
        double speed = sign * spacing;
        double middleCharacterSpeed = event.contains("LEFT") ? 750 : 135;
        double shift = this.frames - currentTime;
        int middleCharacterIndex = characterRenders.size() % 2 == 1 ?
                (characterRenders.size() / 2) + 1 : characterRenders.size() / 2;
        int nextCharacterIndex = event.contains("LEFT")? middleCharacterIndex - 1: middleCharacterIndex + 1;
        if (shift >= 1) {

            ImagePoint nextCharacterImage = imagePointManagerSingleton.getImages().get(nextCharacterIndex);
            nextCharacterImage.setOpacity(1);
            ImagePoint middleCharacterImage = imagePointManagerSingleton.getImages().get(middleCharacterIndex);
            middleCharacterImage.setOpacity(0.5);

            // set scaling
            imagePointManagerSingleton.getImages().get(nextCharacterIndex).setScale(
                    nextCharacterImage.getScale() + (maxScale - nextCharacterImage.getScale())/shift);

            imagePointManagerSingleton.getImages().get(middleCharacterIndex).setScale(
                    middleCharacterImage.getScale() - (middleCharacterImage.getScale() - minScale)/shift);
            // set positioning
            imagePointManagerSingleton.getImages().get(nextCharacterIndex).setPos(
                    nextCharacterImage.getPos().x + sign* Math.abs((540 - nextCharacterImage.getPos().x))/shift,
                    nextCharacterImage.getPos().y - (nextCharacterImage.getPos().y - maxHeight)/shift);
            imagePointManagerSingleton.getImages().get(middleCharacterIndex).setPos(
                    middleCharacterImage.getPos().x + sign * middleCharacterSpeed/calls,
                    middleCharacterImage.getPos().y + (minHeight - middleCharacterImage.getPos().y)/shift);

            for (int i = 0; i < characterRenders.size(); i++ ) {
                if (i != nextCharacterIndex && i != middleCharacterIndex) {
                    ImagePoint characterRender = characterRenders.get(i);

                    // update positions
                    imagePointManagerSingleton.get(characterRender.getFilename()).setPos(characterRender.getPos().x + speed/calls, characterRender.getPos().y);
                }
            }
        }
        // last frame
        if (frames - currentTime == 1) {
            if (event.contains("RIGHT")) {
                ImagePoint temp = imagePointManagerSingleton.getImages().get(0);

                imagePointManagerSingleton.remove(0);
                imagePointManagerSingleton.add(temp);
                temp.setPos(Window.getWidth()/2 + (spacing * (characterRenders.size() - middleCharacterIndex) - spacing), minHeight);
            } else {
                ImagePoint temp = imagePointManagerSingleton.get(imagePointManagerSingleton.getImages().size() - 1);
                imagePointManagerSingleton.remove(imagePointManagerSingleton.getImages().size() - 1);
                imagePointManagerSingleton.add(0, temp);
                temp.setPos(((characterRenders.size() * spacing/2) - Window.getWidth())/2 - 490 - spacing, minHeight);
            }
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
