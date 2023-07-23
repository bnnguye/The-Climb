import bagel.Image;
import bagel.Window;
import bagel.util.Point;

public class PowerUpSpeedUp extends PowerUp{
    private final double speed = 3 + GameSettingsSingleton.getInstance().getMapSpeed();

    public PowerUpSpeedUp() {
        this.name = "SpeedUp";
        pos = new Point(Window.getWidth() * Math.random(), -200);
    }
}
