import bagel.Keys;

import java.util.ArrayList;

public class ButtonLogger {
    private ArrayList<Keys> buttons;
    private String timestamp;

    public ButtonLogger(ArrayList<Keys> buttons, String timestamp) {
        this.buttons = buttons;
        this.timestamp = timestamp;
    }
}

