import java.util.ArrayList;

public class EventsListenerSingleton {

    private static EventsListenerSingleton single_instance = null;
    private static final EventsListener eventsListener = new EventsListener();
    private static boolean canInteract = false;

    public synchronized static EventsListenerSingleton getInstance() {
        if (single_instance == null) {
            single_instance = new EventsListenerSingleton();
        }
        return single_instance;
    }

    public EventsListener getEventsListener() { return eventsListener;}

    public boolean isCanInteract() {
        return canInteract;
    }

    public static class EventsListener {
        ArrayList<EventInterface> events = new ArrayList<>();
        ArrayList<EventInterface> finishedEvents = new ArrayList<>();

        public void addEvent(EventInterface event) {
            events.add(event);
        }

        public void updateEvents() {
            ArrayList<EventInterface> eventsToRemove = new ArrayList<>();
            canInteract = true;
            for (EventInterface event: events) {
                if (!event.isCanInteract()) {
                    canInteract = false;
                }
                if (event.getFrames() <= TimeLogger.getInstance().getFrames()) {
                    System.out.printf("Event finished: %s\n%n", event.getEvent());
                    finishedEvents.add(event);
                    eventsToRemove.add(event);
                }
                else {
                    event.process();
                }
            }
            events.removeAll(eventsToRemove);
        }

        public boolean canInteract() {
            return canInteract;
        }
    }
}
