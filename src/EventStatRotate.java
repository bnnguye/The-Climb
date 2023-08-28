public class EventStatRotate extends EventInterface {
    public EventStatRotate(String event) {
        this.event = event;
        this.canInteract = false;
        this.frames = 20 + timeLogger.getTime();
    }

    public void process() {
        double xoffset = this.event.contains("RIGHT") ? -2000.0/20 : 2000.0/20;
        for (ImagePoint imagePoint: imagePointManagerSingleton.getImages()) {
            if (("characterRender").equalsIgnoreCase(imagePoint.getTag())) {
                imagePoint.move(xoffset, 0);
            }
        }
    }
}
