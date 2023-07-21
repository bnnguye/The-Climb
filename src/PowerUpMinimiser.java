import bagel.Image;
import bagel.Window;
import bagel.util.Point;

public class PowerUpMinimiser extends PowerUp{
    Image image = new Image("res/PowerUp/Minimiser.png");

    public PowerUpMinimiser() {
        this.name = "Minimiser";
        pos = new Point(Window.getWidth() * Math.random(), -200);
    }

    public void gainPowerUp(Player player) {
        player.getCharacter().minimiser();
    }
}
