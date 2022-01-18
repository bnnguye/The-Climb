import bagel.Image;
import bagel.Window;
import bagel.util.Point;

public abstract class PowerUp {
    private String name;
    Image image;
    private Point pos;
    double speed = 3 + GameSettingsSingleton.getInstance().getMapSpeed();


    public PowerUp() {
        pos = new Point(Window.getWidth() * Math.random(), -200);
    }

    public void move() {pos = new Point(pos.x, pos.y + speed);}
    public Image getImage() {return image;}
    public Point getPos() { return pos;}
    public String getName() { return name;}
    public void setJotaroAbility(boolean bool) {}
}
