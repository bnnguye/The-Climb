import bagel.Image;
import bagel.Window;
import bagel.util.Point;

public class Shard {

    Point pos;
    private final double speed = 3;

    public Image getImage() {
        return image;
    }

    Image image = new Image("res/charactersS/Puck/Shard.png");
    private boolean jotaroAbility = false;

    public Shard() {
        this.pos = new Point(Math.random()* Window.getWidth(), -200);
    }

    public void move() {
        if (!jotaroAbility) {
            this.pos = new Point(this.pos.x, this.pos.y + speed);
        }
    }

    public void draw() {
        image.drawFromTopLeft(this.pos.x, this.pos.y);
    }

    public Point getPos() {return this.pos;}

    public void setJotaroAbility(boolean bool) {jotaroAbility = bool;}

}
