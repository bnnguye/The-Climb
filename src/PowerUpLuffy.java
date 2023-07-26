import Enums.PowerUps;
import bagel.Image;
import bagel.util.Point;

public class PowerUpLuffy extends PowerUp {
    private final PowerUps name = PowerUps.LUFFYSARMS;
    private final Image image = new Image("res/PowerUp/Luffy.png");

    void move() {

    }

    Image getImage() {
        return null;
    }

    Point getPos() {
        return null;
    }

    String getName() {
        return null;
    }

    void gainPowerUp(Player player) {

    }

    public void activate(Character character) {
        Luffy luffy = new Luffy(character);
    }

    void adjustOffset(double offset) {
        this.off
    }

    double getSpeed() {
        return 0;
    }


}
