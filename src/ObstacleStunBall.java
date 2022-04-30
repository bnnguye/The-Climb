import bagel.Image;
import bagel.Window;
import bagel.util.Point;

public class ObstacleStunBall extends Obstacle{
    private String name = "StunBall";
    Image image = new Image("res/obstacles/stunball.png");
    private Point pos;
    double speed = 2 + GameSettingsSingleton.getInstance().getMapSpeed();
    double time = 1*SettingsSingleton.getInstance().getFrames();

    public ObstacleStunBall() {
        this.pos = new Point(Window.getWidth() * Math.random(), -200);
    }


    public void move() {
        pos = new Point(pos.x, pos.y + speed);
    }

    public Image getImage() { return this.image;}
    public Point getPos() {return this.pos;}
    public void setPos(Point point) { this.pos = point;}
    public double getTime() {return this.time;}
    public String getName() {return this.name;}
}
