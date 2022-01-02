import bagel.DrawOptions;
import bagel.util.Colour;
import bagel.util.Point;
import bagel.util.Rectangle;

public class ButtonBackToStart extends Button {
    private boolean hovering = false;
    private Point position;
    private Rectangle box;
    private DrawOptions DO = new DrawOptions();

    public ButtonBackToStart(String name, Rectangle rectangle) {
        super(name, rectangle);
        this.position = rectangle.bottomLeft();
        DO.setBlendColour(whiteTranslucent);
        if (java.time.LocalTime.now().getHour() > 18) {
            Colour whiteTranslucent = new Colour(1, 1, 1, 0.5);
            DO.setBlendColour(whiteTranslucent);
        }
    }

    public void playAction() { SettingsSingleton.getInstance().setGameStateString("Menu");}
}
