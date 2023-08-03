import Enums.PowerUps;
import bagel.Image;
import bagel.Window;
import bagel.util.Point;
import bagel.util.Rectangle;

public class PowerUpAbility extends PowerUp {
    private final PowerUps name = PowerUps.ABILITY;
    private final Image image = new Image("res/PowerUp/ABILITY.png");

    public PowerUpAbility() {
        super();
    }

    public PowerUpAbility(Point point) {
        super(point);
    }

    private final double speed = 1 + GameSettingsSingleton.getInstance().getMapSpeed();
    private double offset = 0;

    public void activate(Character character) {
        character.gainSpecialAbilityBar(10);
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

    public void adjustOffset(double offset) {
        this.offset += offset;
    }
    public double getSpeed() {
        return speed;
    }


}