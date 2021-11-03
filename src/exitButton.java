import bagel.Window;
import bagel.util.Rectangle;

public class exitButton extends Button {
    private boolean hovering = false;

    public exitButton(String name, Rectangle rectangle) {
        super(name, rectangle);
    }
    public void playAction() { Window.close(); }
}
