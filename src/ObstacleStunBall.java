import bagel.Image;
import bagel.Window;
import bagel.util.Point;

public class ObstacleStunBall extends Obstacle{
    private String name = "StunBall";
    Image image = new Image("res/obstacles/stunball.png");
    private Point pos;
    double speed = 2 + GameSettingsSingleton.getInstance().getMapSpeed();
    boolean gojoAbility = false;
    boolean jotaroAbility = false;
    private final double frames = SettingsSingleton.getInstance().getFrames();
    double time = 2*frames;

    public ObstacleStunBall() {
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
    public double getTime() {return this.time;}
    public String getName() {return this.name;}
}
