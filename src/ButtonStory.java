import bagel.util.Rectangle;

public class ButtonStory extends Button {
    public ButtonStory(String name, Rectangle rectangle) {
        super(name, rectangle);
    }

    public void playAction() {
        SettingsSingleton.getInstance().setGameMode(0);
        SettingsSingleton.getInstance().setGameState(2);
    }
}