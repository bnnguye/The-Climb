public class Time {

    private int frames;

    public Time(int frames) {
        this.frames = frames;
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
