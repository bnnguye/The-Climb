import bagel.util.Point;

import java.util.ArrayList;

public class ImagePointManagerSingleton {
    private static ImagePointManagerSingleton single_instance = null;
    private ArrayList<ImagePoint> images = new ArrayList<>();
    private static ImagePoint currentBackground = null;

    public void setCurrentBackground(String filename) {
        if (currentBackground == null) {
            currentBackground = new ImagePoint(null, new Point(0,0));
        }
        currentBackground.setImage(filename);
    }

    public synchronized static ImagePointManagerSingleton getInstance() {
        if (single_instance == null) {
            single_instance = new ImagePointManagerSingleton();
        }
        return single_instance;
    }

    public ArrayList<ImagePoint> getImages() {
        return images;
    }

    public void add(ImagePoint imagePoint) {
        images.add(imagePoint);
    }

    public ImagePoint get(String filename) {
        for (ImagePoint imagePoint: images) {
            if (imagePoint.getFilename().equalsIgnoreCase(filename)) {
                return imagePoint;
            }
        }
        return null;
    }

    public ImagePoint getCurrentBackground() {
        if (currentBackground == null) {
            currentBackground = new ImagePoint(null, new Point(0,0));
        }
        return currentBackground;}

//    public void shakeImage() {
//        if (shakeTimer > 0) {
//            if (shakeTimer % 6 == 0) {
//                currentBackgroundPoint = new Point(currentBackgroundPoint.x - 50, currentBackgroundPoint.y);
//            }
//            else if (shakeTimer % 5 == 0) {
//                currentBackgroundPoint = new Point(currentBackgroundPoint.x + 50, currentBackgroundPoint.y);
//            }
//            shakeTimer --;
//        }
//        else {
//            currentBackgroundPoint = new Point(0, 0);
//        }
//    }
//
//    public void transition() {
//        if (transitionTimer >= 3 * frames) {
//            Drawing.drawRectangle(0, 0, Window.getWidth(), Window.getHeight(), new Colour(0, 0, 0, 1 - (transitionTimer - 3*frames)/(2*frames)));
//        }
//        else if ((transitionTimer < 3 * frames) && (transitionTimer > 2 * frames)) {
//            Drawing.drawRectangle(0, 0, Window.getWidth(), Window.getHeight(), new Colour(0,0,0,1));
//        }
//        else if (transitionTimer <= 2 * frames) {
//            Drawing.drawRectangle(0, 0, Window.getWidth(), Window.getHeight(), new Colour(0, 0, 0, transitionTimer/(2*frames)));
//        }
//        if (transitionTimer == 2.5 * frames) {
//            dark = false;
//            if (playingStory) {
//                map = mapToTransitionTo;
//                map.generateMap();
//            }
//            else {
//                changeBackground(sceneToTransitionTo.getFilename());
//            }
//        }
//        transitionTimer--;
//    }
//
//    public void darken() {
//        Drawing.drawRectangle(0, 0, Window.getWidth(), Window.getHeight(), ColourPresets.DARK.toColour());
//    }

    public void draw() {
        for (ImagePoint image: images) {
            image.draw();
        }
    }

    public void drawImagesWithTag(String tag) {
        for (ImagePoint characterRender: getAllImagesWithTag(tag)) {
            characterRender.draw();
        }
    }

    private ArrayList<ImagePoint> getAllImagesWithTag(String tag) {
        ArrayList<ImagePoint> taggedImages = new ArrayList<>();
        for (ImagePoint image: getImages()) {
            if (tag.equals(image.getTag())) {
                taggedImages.add(image);
            }
        }
        return taggedImages;
    }
}
