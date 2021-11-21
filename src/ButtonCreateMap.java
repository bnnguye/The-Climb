import bagel.util.Rectangle;

public class ButtonCreateMap extends Button {
    public ButtonCreateMap(String name, Rectangle rectangle) {
        super(name, rectangle);
    }

    public void playAction() {
        SettingsSingleton.getInstance().setGameState(9);
    }
}