import bagel.Image;
import bagel.Window;
import bagel.util.Point;

public class ObstacleShard {

    private Point pos;
    private final double speed = 3* GameSettingsSingleton.getInstance().getMapSpeed();

    public Image getImage() {
        return image;
    }

    Image image = new Image("res/charactersS/Puck/Shard.png");

    public ObstacleShard() {
        this.pos = new Point(Math.random()* Window.getWidth(), -200);
    }

    public void move() {
        this.pos = new Point(this.pos.x, this.pos.y + speed);
    }

    public void draw() {
        image.drawFromTopLeft(this.pos.x, this.pos.y);
    }

    public Point getPos() {return this.pos;}

}
