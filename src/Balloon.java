import bagel.Image;
import bagel.Window;
import bagel.util.Point;

public class Balloon extends Obstacle {
    private String name = "Balloon";
    Image image = new Image("res/obstacles/bowlingball.png");
    Point pos;
    double speed = 7;
    boolean gojoAbility = false;
    boolean jotaroAbility = false;

    public Balloon() {
        this.pos = new Point(Window.getWidth() * Math.random(), -200);
    }


    public void move() {
        if (!jotaroAbility) {
            if (gojoAbility) {
                pos = new Point(pos.x, pos.y + speed / 2);
            } else {
                pos = new Point(pos.x, pos.y + speed);
            }
        }
    }

    public Image getImage() { return this.image;}
    public Point getPos() {return this.pos;}
    public void setPos(Point point) { this.pos = point;}
    public void setGojoAbility(boolean bool) {
        this.gojoAbility = bool;
    }
    public void setJotaroAbility(boolean bool) {jotaroAbility = bool;}
    public String getName() {return this.name;}
}

