public abstract class EventInterface {

    public int frames;

    public String event;

    public boolean canInteract;

    public void process() {}

    public int getFrames() { return frames;}

    public boolean isCanInteract() { return canInteract;}

    public String getEvent() {
        return event;
    }
}
