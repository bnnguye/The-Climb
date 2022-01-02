import bagel.Image;
import bagel.Window;
import bagel.util.Point;

public abstract class Obstacle {
    private String name = "";
    Image image;
    private Point pos;
    double speed;
    boolean gojoAbility = false;
    boolean jotaroAbility = false;

    public Obstacle() {
        pos = new Point(Window.getWidth() * Math.random(), -200);
    }

    public void move() {
    }

    public Image getImage() { return image;}
    public Point getPos() {return pos;}
    public void setPos(Point point) { pos = point;}
    public void setGojoAbility(boolean bool) { gojoAbility = bool;}
    public void setJotaroAbility(boolean bool) {jotaroAbility = bool;}
    public String getName() {return "";}
}
