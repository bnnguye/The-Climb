import bagel.Image;
import bagel.Window;
import bagel.util.Point;

public class PowerUpSpecialAbilityPoints extends PowerUp {
    private String name = "SpecialAbilityPoints";
    private Point pos;
    Image image = new Image("res/PowerUp/SpecialAbilityPoints.png");

    public PowerUpSpecialAbilityPoints() {
        pos = new Point(Window.getWidth() * Math.random(), -200);
    }

    public Image getImage() {return this.image;}
    public Point getPos() { return this.pos;}
    public void move() {
        this.pos = new Point(pos.x, pos.y + speed);
    }
    public String getName() { return this.name;}

    public void gainPowerUp(Player player) {
        player.getCharacter().gainSpecialAbilityBar(20);
    }

}
