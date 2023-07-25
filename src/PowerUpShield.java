import bagel.Image;
import bagel.Window;
import bagel.util.Point;

public class PowerUpShield extends PowerUp {

    public PowerUpShield() {
        this.name = "Shield";
        this.image = new Image("res/PowerUp/Shield.png");
        this.speed = 3 + GameSettingsSingleton.getInstance().getMapSpeed();
        pos = new Point(Window.getWidth() * Math.random(), -200);
    }

    public PowerUpShield(Point pos) {
        this.pos = pos;
    }

    public void activate(Player player) {
        player.getCharacter().shield();
    }
    public Image getImage() {return image;}


}
