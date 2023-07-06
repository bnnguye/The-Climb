import bagel.Image;
import bagel.Window;
import bagel.util.Point;

public class PowerUpShield extends PowerUp {
    private String name = "Shield";
    private Point pos;
    Image image = new Image("res/PowerUp/Shield.png");

    public PowerUpShield() {
        pos = new Point(Window.getWidth() * Math.random(), -200);
    }

    public PowerUpShield(Point pos) {
        this.pos = pos;
    }

    public Image getImage() {return this.image;}
    public Point getPos() { return this.pos;}
    public void move() {
        this.pos = new Point(pos.x, pos.y + speed);
    }
    public String getName() { return this.name;}

    public void gainPowerUp(Player player) {
        player.getCharacter().shield();
    }

}
