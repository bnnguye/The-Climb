import bagel.Image;
import bagel.Window;
import bagel.util.Point;

public class PowerUpShield extends PowerUp {
    Image image = new Image("res/PowerUp/Shield.png");

    public PowerUpShield() {
        this.name = "Shield";
        pos = new Point(Window.getWidth() * Math.random(), -200);
    }

    public PowerUpShield(Point pos) {
        this.pos = pos;
    }

    public void activate(Player player) {
        player.getCharacter().shield();
    }

}
