import bagel.Image;
import bagel.Window;
import bagel.util.Point;

public abstract class PowerUp {
    protected String name;
    protected Image image;
    protected Point pos;
    protected double offset = 0;
    private double speed;

    public PowerUp() {
        pos = new Point(Window.getWidth() * Math.random(), -200);
    }

    public void move() {pos = new Point(pos.x, pos.y + speed);}
    public Image getImage() {return image;}
    public Point getPos() { return pos;}
    public String getName() { return name;}
    public void gainPowerUp(Player player) {
        if (!player.getCharacter().hasPowerUp()) {
            player.getCharacter().setPowerUp(this);
        }
    }
    public void activate(Character character) {
    }

    public void adjustOffset(double offset) {
        this.offset = offset;
    }
    public double getSpeed() { return this.speed;}

    public void update(){

    }
}
