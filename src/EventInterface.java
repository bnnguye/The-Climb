import java.util.ArrayList;

public abstract class EventInterface {

    ImagePointManagerSingleton imagePointManagerSingleton = ImagePointManagerSingleton.getInstance();
    SettingsSingleton settingsSingleton = SettingsSingleton.getInstance();
    EventsListenerSingleton eventsListenerSingleton = EventsListenerSingleton.getInstance();
    TimeLogger timeLogger = TimeLogger.getInstance();
    int refreshRate = timeLogger.getRefreshRate();

    ArrayList<EventInterface> eventsToBeAdded = new ArrayList<>();

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
