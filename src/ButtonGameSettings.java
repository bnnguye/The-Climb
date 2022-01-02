import bagel.DrawOptions;
import bagel.Font;
import bagel.util.Colour;
import bagel.util.Rectangle;

public class ButtonGameSettings extends Button {

    private Font font = new Font("res/fonts/DejaVuSans-Bold.ttf", 70);
    private Rectangle rectangle;

    public ButtonGameSettings(String name, Rectangle rectangle) {
        super(name, rectangle);
        this.rectangle = rectangle;
    }

    public void playAction() {
        SettingsSingleton.getInstance().setGameState(10);
    }

    public void draw() { font.drawString("SETTINGS", this.rectangle.left(), this.rectangle.top() + 70 , new DrawOptions().setBlendColour(new Colour(0, 0, 0))); }
}
