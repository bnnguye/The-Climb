import bagel.DrawOptions;
import bagel.util.Colour;
import bagel.util.Rectangle;

public class fastButton extends Button {
    DrawOptions DO = new DrawOptions();

    public fastButton(String name, Rectangle rectangle) {
        super(name, rectangle);
        if (java.time.LocalTime.now().getHour() > 18) {
            Colour whiteTranslucent = new Colour(1, 1, 1, 0.5);
            DO.setBlendColour(whiteTranslucent);
        }
    }

    public void playAction() {
        SettingsSingleton.getInstance().setLevel(0);
        SettingsSingleton.getInstance().setGameState(2);}
}