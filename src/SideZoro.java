import bagel.Drawing;
import bagel.Image;
import bagel.Window;
import bagel.util.Colour;
import bagel.util.Point;
import bagel.util.Rectangle;

import java.util.ArrayList;

public class SideZoro extends SideCharacter{
    private final int frames = 144;
    String name = "Zoro";
    String soundPath = String.format("music/%s.wav", this.name);
    Image icon = new Image(String.format("res/charactersS/Zoro/Icon.PNG", this.name));
    boolean activating = false;
    int timer;
    Image selected = new Image(String.format("res/Selected/%s_Selected.png", this.name));
    int shakeTimer;


    public String getName() {
        return this.name;
    }
    public Image getIcon() {return this.icon;}
    public void setIconPos(Point point) {this.iconPos = point;}
    public Point getIconPos() {return this.iconPos;}
    public Image getSelected() {return this.selected;}
    public boolean isActivating() {return this.activating;}
    public void reset() {
        this.activating = false;
        this.animating = false;
        this.timer = 0;
        shakeTimer = 500;
    }
    public String playLine() {return this.soundPath;}

    public void activateAbility(Player user, ArrayList<Player> players, ArrayList<Obstacle> obstacles, ArrayList<PowerUp> powerUps, Map map) {
        ArrayList<Obstacle> obstaclesToRemove = new ArrayList<>();
        Image slashLeft = new Image("res/charactersS/Zoro/SlashLeft.png");
        Image slashMiddle = new Image("res/charactersS/Zoro/SlashMiddle.png");
        Image slashRight = new Image("res/charactersS/Zoro/SlashRight.png");
        Image display = new Image("res/charactersS/Zoro/Activate.png");
        Colour darken = new Colour(0, 0, 0, 0.5);
        Drawing.drawRectangle(0, 0, Window.getWidth(), Window.getHeight(), darken);
        if (!this.activating) {
            timer = 5*frames;
            this.activating = true;
            shakeTimer = 3*frames;
        }
        else {
            if (timer > 3*frames) {
                this.animating = true;
                display.drawFromTopLeft(0,0);
            }
            else {
                shakeImage(map);
                this.animating = false;
                slashLeft.drawFromTopLeft(0, -200);
                slashMiddle.drawFromTopLeft(0, Window.getHeight()/2 - 100);
                slashRight.drawFromTopLeft(0, -200);
                ArrayList<Point> hitbox = new ArrayList<>();
                // bottomleft-topright
                hitbox.add(new Point(354.00, 1065.00));
                hitbox.add(new Point(392.00, 1008.00));
                hitbox.add(new Point(422.00, 958.00));
                hitbox.add(new Point(464.00, 895.00));
                hitbox.add(new Point(499.00, 858.00));
                hitbox.add(new Point(527.00, 815.00));
                hitbox.add(new Point(559.00, 773.00));
                hitbox.add(new Point(607.00, 720.00));
                hitbox.add(new Point(654.00, 665.00));
                hitbox.add(new Point(697.00, 625.00));
                hitbox.add(new Point(734.00, 583.00));
                hitbox.add(new Point(777.00, 530.00));
                hitbox.add(new Point(832.00, 482.00));
                hitbox.add(new Point(880.00, 437.00));
                hitbox.add(new Point(944.00, 393.00));
                hitbox.add(new Point(1002.00, 343.00));
                hitbox.add(new Point(1060.00, 305.00));
                hitbox.add(new Point(1107.00, 260.00));
                hitbox.add(new Point(1164.00, 220.00));
                hitbox.add(new Point(1224.00, 180.00));
                hitbox.add(new Point(1277.00, 150.00));
                hitbox.add(new Point(1335.00, 119.00));
                hitbox.add(new Point(1399.00, 85.00));
                hitbox.add(new Point(1452.00, 58.00));
                hitbox.add(new Point(1544.00, 30.00));
                //bottomright-topleft
                hitbox.add(new Point(1321.00, 1050.00));
                hitbox.add(new Point(1289.00, 992.00));
                hitbox.add(new Point(1249.00, 939.00));
                hitbox.add(new Point(1202.00, 873.00));
                hitbox.add(new Point(1161.00, 815.00));
                hitbox.add(new Point(1119.00, 765.00));
                hitbox.add(new Point(1079.00, 715.00));
                hitbox.add(new Point(1037.00, 670.00));
                hitbox.add(new Point(999.00, 628.00));
                hitbox.add(new Point(956.00, 583.00));
                hitbox.add(new Point(909.00, 540.00));
                hitbox.add(new Point(879.00, 505.00));
                hitbox.add(new Point(849.00, 479.00));
                hitbox.add(new Point(804.00, 448.00));
                hitbox.add(new Point(754.00, 400.00));
                hitbox.add(new Point(707.00, 363.00));
                hitbox.add(new Point(649.00, 315.00));
                hitbox.add(new Point(562.00, 255.00));
                hitbox.add(new Point(519.00, 223.00));
                hitbox.add(new Point(464.00, 195.00));
                hitbox.add(new Point(414.00, 158.00));
                hitbox.add(new Point(357.00, 123.00));
                hitbox.add(new Point(307.00, 100.00));
                hitbox.add(new Point(264.00, 78.00));
                hitbox.add(new Point(224.00, 60.00));
                hitbox.add(new Point(177.00, 43.00));
                hitbox.add(new Point(127.00, 23.00));
                hitbox.add(new Point(54.00, 9.00));
                //middleleft-middleright
                hitbox.add(new Point(15.00, 712.00));
                hitbox.add(new Point(82.00, 665.00));
                hitbox.add(new Point(135.00, 635.00));
                hitbox.add(new Point(192.00, 610.00));
                hitbox.add(new Point(260.00, 583.00));
                hitbox.add(new Point(332.00, 563.00));
                hitbox.add(new Point(405.00, 538.00));
                hitbox.add(new Point(493.00, 514.00));
                hitbox.add(new Point(567.00, 500.00));
                hitbox.add(new Point(662.00, 493.00));
                hitbox.add(new Point(742.00, 478.00));
                hitbox.add(new Point(827.00, 473.00));
                hitbox.add(new Point(920.00, 465.00));
                hitbox.add(new Point(1048.00, 464.00));
                hitbox.add(new Point(1145.00, 464.00));
                hitbox.add(new Point(1285.00, 472.00));
                hitbox.add(new Point(1400.00, 477.00));
                hitbox.add(new Point(1540.00, 499.00));
                hitbox.add(new Point(1647.00, 524.00));
                hitbox.add(new Point(1737.00, 547.00));
                hitbox.add(new Point(1835.00, 577.00));
                hitbox.add(new Point(1910.00, 609.00));

                for (Player player: players) {
                    if (player.getId() != user.getId()) {
                        if(!player.isDead()) {
                            for (Point point: hitbox) {
                                Rectangle rectangle = new Rectangle(point, 5,5);
                                if(player.getCharacter().getImage().getBoundingBoxAt(player.getCharacter().getPos()).intersects(rectangle)) {
                                    player.setDead();
                                    break;
                                }
                            }
                        }
                    }
                }
                for (Obstacle obstacle: obstacles) {
                    for (Point point: hitbox) {
                        Rectangle rectangle = new Rectangle(point, 5,5);
                        if(obstacle.getImage().getBoundingBoxAt(obstacle.getPos()).intersects(rectangle)) {
                            obstaclesToRemove.add(obstacle);
                            break;
                        }
                    }
                }
                obstacles.removeAll(obstaclesToRemove);
            }
            timer--;
        }
        if (timer <= 0) {
            this.activating = false;
            shakeTimer = 500;
        }
    }


    public boolean isAnimating() {
        return this.animating;
    }

    public void shakeImage(Map map) {
        if (shakeTimer == 3 *frames) {
            for (Tile tile: map.getTiles()) {
                tile.setPos(new Point(tile.getPos().x + 100, tile.getPos().y + 100));
            }
        }
        if (shakeTimer > 0) {
            for (Tile tile : map.getTiles()) {
                if (shakeTimer % 6 == 0) {
                    tile.setPos(new Point(tile.getPos().x - 300, tile.getPos().y - 200));
                } else if (shakeTimer % 5 == 0) {
                    tile.setPos(new Point(tile.getPos().x + 300, tile.getPos().y + 200));
                }
            }
        }
        if (shakeTimer == 1) {
            for (Tile tile: map.getTiles()) {
                tile.setPos(new Point(tile.getPos().x - 100, tile.getPos().y - 100));
            }
        }
        shakeTimer --;
    }
    
}