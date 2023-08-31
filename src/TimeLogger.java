import java.awt.*;

public class TimeLogger {
    private static TimeLogger single_instance = null;

    private final int refreshRate;
    private int frames = 0;

    public TimeLogger() {
        int refreshRate = 60;
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] gs = ge.getScreenDevices();

        for (GraphicsDevice g : gs) {
            DisplayMode dm = g.getDisplayMode();

            refreshRate = dm.getRefreshRate();
            if (refreshRate == DisplayMode.REFRESH_RATE_UNKNOWN) {
                System.out.println("Unknown rate");
            }
            else {
                System.out.println("Refresh rate: " + refreshRate);
                break;
            }
        }
        this.refreshRate = refreshRate;
    }

    public synchronized static TimeLogger getInstance() {
        if (single_instance == null) {
            single_instance = new TimeLogger();
        }
        return single_instance;
    }

    public void updateTime() {
        frames++;
    }

    public int getTime() {
        return frames;
    }

    public String getDisplayTime() {
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

    public int getRefreshRate() {return refreshRate;}
}
