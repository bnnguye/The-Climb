import bagel.Keys;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class TimeLogger {
    private static TimeLogger single_instance = null;
    private ArrayList<ButtonLogger> buttonsLogger = new ArrayList<>();

    private int hours;
    private int minutes;
    private int seconds;
    private int frames;
    private String currentTime;
    private String displayTime;

    public TimeLogger() {
        frames = 0;
        seconds = 0;
        minutes = 0;
        hours = 0;
        currentTime = "000000000";
        displayTime = "00h00m00s000f";
    }

    public synchronized static TimeLogger getInstance() {
        if (single_instance == null) {
            single_instance = new TimeLogger();
        }
        return single_instance;
    }

    public void updateTime() {
        frames++;
        if (frames >= 144) {
            frames = 0;
            seconds ++;
        }
        if (seconds >= 60) {
            seconds = 0;
            minutes++;
        }
        if (minutes >= 60) {
            minutes = 0;
            hours++;
        }
        currentTime = convertToDoubleDigit(hours) + convertToDoubleDigit(minutes) +
                convertToDoubleDigit(seconds) + convertToTripleDigit(frames);
        displayTime = convertToDoubleDigit(hours) + "h" + convertToDoubleDigit(minutes) + "m"
                + convertToDoubleDigit(seconds) + "s";
    }

    public int getHours() {
        return hours;
    }

    public int getMinutes() {
        return minutes;
    }

    public int getSeconds() {
        return seconds;
    }

    public int getFrames() {
        return frames;
    }

    public void updateButtonsLogger(ArrayList<Keys> buttons) {
        buttonsLogger.add(new ButtonLogger(buttons, currentTime));
    }

    public String convertToDoubleDigit(int num) {
        if (num < 10) {
            return String.format("0%d", num);
        }
        return String.format("%d", num);
    }

    public String convertToTripleDigit(int frames) {
        if (frames < 10) {
            return String.format("00%d", frames);
        }
        else if (frames < 100) {
            return String.format("0%d", frames);
        }
        return String.format("%d", frames);
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public String getDisplayTime() {
        return displayTime;
    }
}
