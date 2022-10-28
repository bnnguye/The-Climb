import bagel.Image;
import bagel.Window;
import bagel.util.Point;

public class ObstacleBall extends Obstacle {
    private String name = "Ball";
    private Image image = new Image("res/obstacle/ball.png");
    private Point pos;
    private double speed = 6 + GameSettingsSingleton.getInstance().getMapSpeed();

    public ObstacleBall() {
        this.pos = new Point(Window.getWidth() * Math.random(), -200);
    }


    public void move() {
        pos = new Point(pos.x, pos.y + speed);
    }

    public Image getImage() { return this.image;}
    public Point getPos() {return this.pos;}
    public void setPos(Point point) { this.pos = point;}
    public String getName() {return this.name;}
}

