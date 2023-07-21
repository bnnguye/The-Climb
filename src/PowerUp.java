import bagel.Image;
import bagel.Window;
import bagel.util.Point;

public abstract class PowerUp {
    String name;
    Image image;
    Point pos;
    double speed = 3 + GameSettingsSingleton.getInstance().getMapSpeed();
    boolean jotaroAbility = false;


    public PowerUp() {
        pos = new Point(Window.getWidth() * Math.random(), -200);
    }

    public void move() {pos = new Point(pos.x, pos.y + speed);}
    public Image getImage() {return image;}
    public Point getPos() { return pos;}
    public String getName() { return name;}
    public void setJotaroAbility(boolean bool) {
        jotaroAbility = bool;
    }
    public boolean canMove() {
        return !jotaroAbility;
    }
    public void gainPowerUp(Player player) {
        if (!player.getCharacter().hasPowerUp()) {
            player.getCharacter().setPowerUp(this);
        }
    }
    public void activate(Character character) {}
}
