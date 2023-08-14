public class EventStatRotate extends EventInterface {
    public EventStatRotate(String event) {
        this.event = event;
        this.canInteract = false;
        this.frames = refreshRate + timeLogger.getFrames();
    }

    public void process() {
        double xoffset = this.event.contains("LEFT") ? -1000.0/refreshRate : 1000.0/refreshRate;
        for (ImagePoint imagePoint: imagePointManagerSingleton.getImages()) {
            if (imagePoint.getTag().equalsIgnoreCase("characterRender")) {
                imagePoint.move(xoffset, 0);
            }
        }
    }
}
