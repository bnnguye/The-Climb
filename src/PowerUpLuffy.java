import Enums.PowerUps;
import bagel.Image;
import bagel.util.Point;

public class PowerUpLuffy extends PowerUp {
    private final PowerUps name = PowerUps.LUFFYSARMS;

    Image image = new Image("res/PowerUp/Luffy.png");

    void move() {

    }

    @Override
    Image getImage() {
        return null;
    }

    @Override
    Point getPos() {
        return null;
    }

    @Override
    String getName() {
        return null;
    }

    @Override
    void gainPowerUp(Player player) {

    }

    public void activate(Character character) {
        Luffy luffy = new Luffy(character);
    }

    @Override
    void adjustOffset(double offset) {

    }

    @Override
    double getSpeed() {
        return 0;
    }


}
