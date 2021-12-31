import bagel.DrawOptions;
import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;

public class ButtonRightArrow extends Button {
    GameSettingsSingleton gameSettingsSingleton = new GameSettingsSingleton();
    Image image = new Image("res/RightArrow.png");
    Point position;

    public ButtonRightArrow(String name, Rectangle rectangle) {
        super(name, rectangle);
        position = rectangle.topLeft();
    }

    public void draw() {
        if (image != null) {
            image.drawFromTopLeft(position.x - 50, position.y - 50, new DrawOptions().setScale(0.5, 0.5));
        }
    }

    public void playAction() {
        if (gameSettingsSingleton.getInstance().getPage() < 1) {
            gameSettingsSingleton.getInstance().setPage(gameSettingsSingleton.getInstance().getPage() + 1);
        }
    }
    public Image getImage() {return this.image;}
}
