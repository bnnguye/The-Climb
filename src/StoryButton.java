import bagel.util.Rectangle;

public class StoryButton extends Button {
    public StoryButton(String name, Rectangle rectangle) {
        super(name, rectangle);
    }

    public void playAction() {
        SettingsSingleton.getInstance().setLevel(99);
        SettingsSingleton.getInstance().setGameState(2);
    }
}