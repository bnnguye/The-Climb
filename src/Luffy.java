import bagel.DrawOptions;
import bagel.Drawing;
import bagel.Image;
import bagel.util.Colour;
import bagel.util.Point;
import bagel.util.Rectangle;

import java.util.ArrayList;
import java.util.Set;

public class Luffy {
    private Character user;
    private final ArrayList<Arm> arms = new ArrayList<>();

    public Luffy(Character user) {
        this.user = user;
        Point position = user.getPos();
        arms.add(new Arm(new Rectangle(position, 0, 50), "Left"));
        arms.add(new Arm(new Rectangle(position, 0, 50), "Right"));
    }

    public void update() {
        double speed = GameSettingsSingleton.getInstance().getMapSpeed();

        ArrayList<Arm> armsToRemove = new ArrayList<>();

        for (Arm arm: arms) {
            arm.extend(speed);
            for (Player player: SettingsSingleton.getInstance().getPlayers()) {
                if (arm.getRectangle().intersects(player.getCharacter().getRectangle())) {
                    player.getCharacter().reduceLive();
                    armsToRemove.add(arm);
                }
            }
        }

        arms.removeAll(armsToRemove);
    }

    public void render() {
        for (Arm arm: arms) {
            arm.render();
        }
    }

    public static class Arm {
        public String tag;
        public Rectangle rectangle;

        public Arm(Rectangle rectangle, String tag) {
            this.rectangle = rectangle;
            this.tag = tag;
        }

        public void render() {
            Drawing.drawRectangle(rectangle.topLeft(), rectangle.right() - rectangle.left(), 50, new Colour(98/255d, 75/255d, 56/255d));
            new Image("res/misc/Hand.png").drawFromTopLeft(rectangle.topRight().x, rectangle.topRight().y, new DrawOptions().setRotation(tag.equalsIgnoreCase("LEFT")?90:0));
        }

        public void extend(double speed) {
            rectangle = new Rectangle(rectangle.topLeft().x + (tag.equalsIgnoreCase("LEFT")?50:0), rectangle.topLeft().y, rectangle.right() - rectangle.left() + speed, 50);
        }

        public Rectangle getRectangle() { return rectangle;}

    }
}
