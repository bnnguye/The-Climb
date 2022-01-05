import bagel.DrawOptions;
import bagel.Image;
import bagel.Window;
import bagel.util.Point;

public class ObstacleRock extends Obstacle {
    private String name = "Rock";
    Image image = new Image("res/obstacles/rock.png");
    private Point pos;
    double speed = 3 + GameSettingsSingleton.getInstance().getMapSpeed();;
    boolean gojoAbility = false;
    boolean jotaroAbility = false;

    public ObstacleRock() {
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
