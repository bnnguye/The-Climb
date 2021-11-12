import bagel.DrawOptions;
import bagel.util.Point;
import bagel.util.Rectangle;

public class RetryButton extends Button {
    private boolean hovering = false;
    private Point position;
    private Rectangle box;
    private DrawOptions DO = new DrawOptions();

    public RetryButton(String name, Rectangle rectangle) {
        super(name, rectangle);
        this.position = rectangle.bottomLeft();
        DO.setBlendColour(blackTranslucent);
    }

    public void playAction() {
        if (SettingsSingleton.getInstance().getLevel() < 99) {
            SettingsSingleton.getInstance().setGameStateString("Retry");
        }
        else {
            SettingsSingleton.getInstance().setGameStateString("Continue");
        }
    }
}
