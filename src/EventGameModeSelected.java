public class EventGameModeSelected extends EventInterface {

    public EventGameModeSelected(int frames, String event) {
        this.frames = frames + TimeLogger.getInstance().getFrames();
        this.event = event;
    }

    public void process() {
//        int refreshRate = SettingsSingleton.getInstance().getRefreshRate();
//        int currentTime = TimeLogger.getInstance().getFrames();
//        if (frames - refreshRate > currentTime) {
//            String storyMenuFileName = "res/menu/StoryMenu.png";
//            String vsMenuFileName = "res/menu/vsMenu.png";
//            ImagePoint storyImage = ImagePointManagerSingleton.getInstance().get(storyMenuFileName);
//            ImagePoint vsImage = ImagePointManagerSingleton.getInstance().get(vsMenuFileName);
//            if (event.contains("STORY")) {
//                storyImage.setPos(storyImage.getPos().x , storyImage.getPos().y);
//                vsImage.setPos();
//            }
//            else if (event.contains("VS")) {
//                storyImage.setPos();
//                vsImage.setPos();
//            }
//        }
    }

}
