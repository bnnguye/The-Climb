import bagel.Image;
import bagel.Window;
import bagel.util.Point;

public class SpeedDown extends PowerUp{
    private String name = "SpeedDown";
    private Point pos;
    Image image = new Image("res/PowerUp/SpeedDown.png");

    public SpeedDown() {
        pos = new Point(Window.getWidth() * Math.random(), -200);
    }

    public Image getImage() {return this.image;}
    public Point getPos() { return this.pos;}
    public void move() {
        this.pos = new Point(pos.x, pos.y + speed);
    }
    public String getName() { return this.name;}

}
