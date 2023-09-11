import Enums.PowerUps;
import bagel.Image;
import bagel.Window;
import bagel.util.Point;
import bagel.util.Rectangle;

public class PowerUpAbility extends PowerUp {
    private final PowerUps type = PowerUps.ABILITY;
    private final Image image = new Image("res/PowerUps/ABILITY.png");

    public PowerUpAbility() {
        super();
    }

    public PowerUpAbility(Point point) {
        super(point);
    }

    public PowerUps getType() { return type; }

    private final double speed = 1 + GameSettingsSingleton.getInstance().getMapSpeed();

    public void activate(Character character) {
        MusicPlayer.getInstance().addMusic("music/misc/AbilityGain.wav");
        character.gainSpecialAbilityBar(20);
    }

    public void draw() {
        image.drawFromTopLeft(pos.x, pos.y);
    }

    public void draw(double x, double y) {
        image.drawFromTopLeft(pos.x + x, pos.y + y);
    }

    public void move() {
        this.pos = new Point(pos.x, pos.y + speed + offset);
    }

    public Rectangle getRectangle() {
        return new Rectangle(pos, image.getWidth(), image.getHeight());
    }


    public double getSpeed() {
        return speed;
    }


}
