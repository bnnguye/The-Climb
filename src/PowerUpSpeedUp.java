import bagel.Image;
import bagel.Window;
import bagel.util.Point;

public class PowerUpSpeedUp extends PowerUp{
    private String name = "SpeedUp";
    private Point pos;
    Image image = new Image("res/PowerUp/SpeedQuestion.png");

    public PowerUpSpeedUp() {
        pos = new Point(Window.getWidth() * Math.random(), -200);
    }
}
