import bagel.Window;
import bagel.util.Rectangle;

public class ButtonExit extends Button {
    private boolean hovering = false;

    public ButtonExit(String name, Rectangle rectangle) {
        super(name, rectangle);
    }
    public void playAction() { Window.close(); }
}
