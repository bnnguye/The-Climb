import bagel.DrawOptions;
import bagel.util.Point;
import bagel.util.Rectangle;

public class playButton extends Button {
    private boolean hovering = false;
    private Point position;
    private Rectangle box;
    private DrawOptions DO = new DrawOptions();

    public playButton(String name, Rectangle rectangle) {
        super(name, rectangle);
        this.position = rectangle.bottomLeft();
        DO.setBlendColour(blackTranslucent);
    }

    public void playAction() { SettingsSingleton.getInstance().setGameState(1);}
}
