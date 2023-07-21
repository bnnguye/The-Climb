import bagel.Image;
import bagel.util.Point;

public class PowerUpLuffy extends PowerUp {

    Image image = new Image("res/PowerUp/Luffy.png");

    public PowerUpLuffy(Point pos) {
        this.name = "Luffy's Arms";
        this.pos = pos;
    }

    public void activate(Character character) {
        Luffy luffy = new Luffy(character);
    }


}
