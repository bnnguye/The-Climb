import bagel.DrawOptions;
import bagel.Image;
import bagel.Window;
import bagel.util.Point;

public class ObstacleRock extends Obstacle {
    private String name = "Rock";
    Image image = new Image("res/obstacle/Rock.png");
    private Point pos;
    double speed = 3 + GameSettingsSingleton.getInstance().getMapSpeed();

    public ObstacleRock() {
        this.pos = new Point(Window.getWidth() * Math.random(), -200);
    }

    public ObstacleRock(Point pos) {
        this.pos = pos;
    }


    public void move() {
        pos = new Point(pos.x, pos.y + speed);
    }

    public Image getImage() { return this.image;}
    public Point getPos() {return this.pos;}
    public void setPos(Point point) { this.pos = point;}
    public String getName() {return this.name;}
}
