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

        public EventInterface getEvent(Class<?> eventClass) {
            for (EventInterface event: events) {
                if (eventClass == event.getClass()) {
                    return event;
                }
            }
            return null;
        }

        public void updateEvents() {
            ArrayList<EventInterface> eventsToRemove = new ArrayList<>();
            ArrayList<EventInterface> eventsToAdd = new ArrayList<>();
            canInteract = true;
            for (EventInterface event: events) {
                if (event.getFrames() <= TimeLogger.getInstance().getTime()) {
                    System.out.printf("Event finished: %s\n%n", event.getEvent());
                    finishedEvents.add(event);
                    eventsToRemove.add(event);
                }
                else {
                    if (!event.isCanInteract()) {
                        canInteract = false;
                    }
                    event.process();
                    eventsToAdd.addAll(event.eventsToBeAdded);
                    event.eventsToBeAdded.clear();
                }
            }
            events.removeAll(eventsToRemove);
            events.addAll(eventsToAdd);
            eventsToAdd.clear();
        }

        public boolean canInteract() {
            return canInteract;
        }

        public boolean contains(Class<?> classInstance) {
            for (EventInterface event: events) {
                if (event.getClass() == classInstance) {
                    return true;
                }
            }
            return false;
        }
    }
}
