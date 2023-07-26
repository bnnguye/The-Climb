import bagel.Window;

import java.util.ArrayList;

public class EventMapRotate extends EventInterface {

    int duration;
    ImagePointManagerSingleton imagePointManagerSingleton = ImagePointManagerSingleton.getInstance();
    ArrayList<ImagePoint> mapImages;

    public EventMapRotate(String direction) {
        int duration = TimeLogger.getInstance().getRefreshRate()/8;
        this.duration = duration;
        this.frames = duration + TimeLogger.getInstance().getFrames();
        this.event = direction;
    }

    public void process() {
        if (mapImages == null) {
            getAllMapImages();
        }
        double spacing = 50;
        int index = 0;
        double minScale = 0.2;
        double maxScale = 0.3;
        int middleIndex = mapImages.size() % 2 == 1 ? mapImages.size() / 2 + 1 : mapImages.size() / 2;

        if (frames - TimeLogger.getInstance().getFrames() - duration == 0) {
            mapImages.get(middleIndex).setScale(minScale);
            mapImages.get(middleIndex).setOpacity(0.3);
        }

        for (ImagePoint mapRender : mapImages) {
            mapRender.setScale(minScale);
            mapRender.setOpacity(0.3);

            double xOffset;
            double yOffset;
            if (event.contains("DOWN")) {
                xOffset = index >= middleIndex ? 400 : -400;
                yOffset = index == middleIndex ? maxScale*mapRender.getHeight() + spacing : minScale*mapRender.getHeight() + spacing;
            }
            else {
                xOffset = index <= middleIndex ? 400 : -400;
                yOffset = index - middleIndex == 1 ? -(maxScale*mapRender.getHeight() + spacing) : -(minScale*mapRender.getHeight() + spacing);
            }

            mapRender.move(xOffset / duration, yOffset / duration);
            index++;
        }

        if (frames - TimeLogger.getInstance().getFrames() == 1) {
            double xOffset = 400;

            if (event.contains("DOWN")) {
                ImagePoint temp = imagePointManagerSingleton.get(mapImages.get(mapImages.size()-1));
                imagePointManagerSingleton.remove(temp);
                imagePointManagerSingleton.add(0, temp);
                temp.setPos(Window.getWidth()/2 - (maxScale*temp.getWidth()/2) + (middleIndex * xOffset),
                        (Window.getHeight()/2 - (maxScale*temp.getHeight()/2) +
                                -middleIndex * (spacing + (temp.getHeight()*temp.getScale()))));

                getAllMapImages();
                ImagePoint mapRender = mapImages.get(middleIndex);
                mapRender.setScale(maxScale);
                mapRender.setOpacity(1);


            }
            else {
                ImagePoint temp = imagePointManagerSingleton.get(mapImages.get(0));
                imagePointManagerSingleton.remove(temp);
                imagePointManagerSingleton.add(temp);
                temp.setPos(Window.getWidth()/2 - (maxScale*temp.getWidth()/2) + ((middleIndex-1) * xOffset),
                        (Window.getHeight()/2 + (maxScale*temp.getHeight()/2) + spacing
                                + (middleIndex-2) * (spacing + (temp.getHeight()*minScale))));

                getAllMapImages();
                ImagePoint mapRender = mapImages.get(middleIndex);
                mapRender.setScale(maxScale);
                mapRender.setOpacity(1);
            }
        }
    }

    private void getAllMapImages() {
        ArrayList<ImagePoint> mapImages = new ArrayList<>();
        for (ImagePoint image: imagePointManagerSingleton.getImages()) {
            if ("MapRender".equals(image.getTag())) {
                mapImages.add(image);
            }
        }
        this.mapImages = mapImages;
    }
}
