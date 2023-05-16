public class TimeLogger {
    private static TimeLogger single_instance = null;

    private int frames = 0;

    public synchronized static TimeLogger getInstance() {
        if (single_instance == null) {
            single_instance = new TimeLogger();
        }
        return single_instance;
    }

    public void updateFrames() {
        frames++;
    }

    public int getFrames() {
        return frames;
    }

    public String getDisplayTime() {
        int refreshRate = SettingsSingleton.getInstance().getRefreshRate();

        int tempFrames = frames;
        int hours = tempFrames/refreshRate/60/60;
        tempFrames -= hours*refreshRate*60*60;
        int minutes = tempFrames/refreshRate/60;
        tempFrames -= minutes*refreshRate*60;
        int seconds = tempFrames/refreshRate;
        tempFrames -= seconds*refreshRate;
        int milliseconds = (int) Math.round(tempFrames/14.4);

        return String.format("%dh%dm%ds%02dms", hours, minutes, seconds, milliseconds);
    }

}
