import bagel.Image;
import bagel.Window;
import bagel.util.Point;

public class PowerUpSpeedUp extends PowerUp{
    private String name = "SpeedUp";
    private Point pos;
    Image image = new Image("res/PowerUp/SpeedUp.png");
    private double speedTime = 3 * SettingsSingleton.getInstance().getFrames();

    public PowerUpSpeedUp() {
        pos = new Point(Window.getWidth() * Math.random(), -200);
    }

    public Image getImage() {return this.image;}
    public Point getPos() { return this.pos;}
    public void move() {
        this.pos = new Point(pos.x, pos.y + speed);
    }
    public String getName() { return this.name;}
    public double getSpeedTime() {
        return speedTime;
    }

    public void gainPowerUp(Player player) {
        player.getCharacter().speedUp();
    }
}
