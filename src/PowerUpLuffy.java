//import Enums.PowerUps;
//import bagel.Image;
//import bagel.util.Point;
//import bagel.util.Rectangle;
//
//public class PowerUpLuffy extends PowerUp {
//    private final PowerUps name = PowerUps.LUFFYSARMS;
//    private final Image image = new Image("res/PowerUps/Luffy.png");
//    private double offset = 0;
//    private double speed = 3 + GameSettingsSingleton.getInstance().getMapSpeed();
//
//    public void activate(Character character) {
//        character.shield();
//    }
//
//    public void draw() {
//        image.drawFromTopLeft(pos.x, pos.y);
//    }
//
//    public void move() {
//        this.pos = new Point(pos.x, pos.y + speed + offset);
//    }
//
//    public Rectangle getRectangle() {
//        return new Rectangle(pos, image.getWidth(), image.getHeight());
//    }
//
//    public void adjustOffset(double offset) {
//        this.offset += offset;
//    }
//    public double getSpeed() {
//        return speed;
//    }
//
//
//}
