import java.util.ArrayList;

public class EventMapRotate extends EventInterface {


    ImagePointManagerSingleton imagePointManagerSingleton = ImagePointManagerSingleton.getInstance();
    ArrayList<ImagePoint> mapImages;

    public EventMapRotate(String direction) {
        int duration = SettingsSingleton.getInstance().getRefreshRate()/8;
        this.frames = duration + TimeLogger.getInstance().getFrames();
        this.event = direction;
    }

    public void process() {
        if (mapImages == null) {
            getAllMapImages();
        }

        int middleIndex = mapImages.size() % 2 == 1 ? mapImages.size() / 2 + 1 : mapImages.size() / 2;
        double sign = event.contains("LEFT") ? 1 : -1;

        int index = 0;
        for (int i = 0; i < mapImages.size(); i++) {
            
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
