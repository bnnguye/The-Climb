import bagel.Image;
import bagel.Window;
import bagel.util.Point;

public class PowerUpMinimiser extends PowerUp{

    public PowerUpMinimiser() {
        this.name = "Minimiser";
        this.image = new Image("res/PowerUp/Minimiser.png");
        this.pos = new Point(Window.getWidth() * Math.random(), -200);
    }

    public void gainPowerUp(Player player) {
        player.getCharacter().minimiser();
    }
    public Image getImage() {return image;}

}
