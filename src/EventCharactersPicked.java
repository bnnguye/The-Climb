public class EventCharactersPicked extends EventInterface {

    private boolean cannotInteract = false;

    public EventCharactersPicked(int frames, String event) {
        this.event = event;
        this.frames = frames;
    }

    public void process() {

    }
}
