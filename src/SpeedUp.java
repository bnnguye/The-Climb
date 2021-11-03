import bagel.Image;
import bagel.Window;
import bagel.util.Point;

public class SpeedUp extends PowerUp{
    String name = "SpeedUp";
    Point pos;
    Image image = new Image("res/PowerUp/SpeedUp.png");
    boolean jotaroAbility = false;

    public SpeedUp() {
        pos = new Point(Window.getWidth() * Math.random(), -200);
    }

    public Image getImage() {return this.image;}
    public Point getPos() { return this.pos;}
    public void move() {
        if (!jotaroAbility) {
            this.pos = new Point(pos.x, pos.y + speed);
        }
    }
    public String getName() { return this.name;}
    public void setJotaroAbility(boolean bool) {jotaroAbility = bool;}
}
