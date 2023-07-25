import bagel.Image;
import bagel.Window;
import bagel.util.Point;

public class PowerUpSpecialAbilityPoints extends PowerUp {
    private final double speed = 1 + GameSettingsSingleton.getInstance().getMapSpeed();


    public PowerUpSpecialAbilityPoints() {
        this.name = "SpecialAbilityPoints";
        this.image = new Image("res/PowerUp/SpecialAbilityPoints.png");
        this.pos = new Point(Window.getWidth() * Math.random(), -200);
    }

    public void move() {
        this.pos = new Point(pos.x, pos.y + speed + offset);
    }
    public String getName() { return this.name;}

    public void gainPowerUp(Player player) {
        player.getCharacter().gainSpecialAbilityBar(20);
    }
    public Image getImage() {return image;}


}
