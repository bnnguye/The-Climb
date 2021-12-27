import bagel.DrawOptions;
import bagel.util.Point;
import bagel.util.Rectangle;

public class ButtonRetry extends Button {
    private boolean hovering = false;
    private Point position;
    private Rectangle box;
    private DrawOptions DO = new DrawOptions();

    public ButtonRetry(String name, Rectangle rectangle) {
        super(name, rectangle);
        this.position = rectangle.bottomLeft();
        DO.setBlendColour(blackTranslucent);
    }

    public void playAction() {
        if (SettingsSingleton.getInstance().getGameMode() < 99) {
            SettingsSingleton.getInstance().setGameStateString("Retry");
        }
        else {
            SettingsSingleton.getInstance().setGameStateString("Continue");
        }
    }
}
