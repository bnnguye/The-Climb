import bagel.DrawOptions;
import bagel.util.Colour;
import bagel.util.Rectangle;

public class fasterButton extends Button {
    DrawOptions DO = new DrawOptions();

    public fasterButton(String name, Rectangle rectangle) {
        super(name, rectangle);
        if (java.time.LocalTime.now().getHour() > 18) {
            Colour whiteTranslucent = new Colour(1, 1, 1, 0.5);
            DO.setBlendColour(whiteTranslucent);
        }
    }

    public void playAction() {
        SettingsSingleton.getInstance().setLevel(1);
        SettingsSingleton.getInstance().setGameState(2);}
}