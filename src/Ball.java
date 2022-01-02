import bagel.Image;
import bagel.Window;
import bagel.util.Point;

public class Ball extends Obstacle {
    private String name = "Ball";
    private Image image = new Image("res/obstacles/bowlingball.png");
    private Point pos;
    private double speed = 6 + GameSettingsSingleton.getInstance().getMapSpeed();;
    private boolean gojoAbility = false;
    private boolean jotaroAbility = false;

    public Ball() {
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

