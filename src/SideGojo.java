import bagel.Drawing;
import bagel.Image;
import bagel.Window;
import bagel.util.Colour;
import bagel.util.Point;

import java.util.ArrayList;

public class SideGojo extends SideCharacter{
    private final double frames = SettingsSingleton.getInstance().getRefreshRate();
    private String name = "GOJO SATORU";
    private String power = "MUGEN";
    private String desc = "Gojo Satoru's ability \"Mugen (Infinity)\" is the neutral form of Gojo's base ability" +
            "\"Mukagen (Limitless)\". When activated, Gojo invokes a gravitational hold in the middle of the battlefield." +
            "During this time, players will be slowed and gravitate towards the center.";

    boolean activating = false;
    boolean animating = false;
    double timer;

    int radius = 0;

    public String getName() {
        return this.name;
    }
    public String getPower() { return this.power;}
    public String getDesc() { return this.desc;}

    public boolean isActivating() {return this.activating;}
    public boolean isAnimating() {
        return this.animating;
    }
    public void reset() {
        this.activating = false;
        this.animating = false;
        timer = 0;
    }
    public String getSoundPath() {return String.format("music/%s.wav", this.name);}

    public void activateAbility(Player user, ArrayList<Player> players, ArrayList<Obstacle> obstacles, ArrayList<PowerUp> powerUps, Map map) {
        if(!this.activating) {
            this.activating = true;
            this.timer = 8 * frames;
        }

        if (this.timer > 6*frames) {
            this.animating = true;
        }
        else {
            this.animating = false;
            warp(players, user);
            for (Player player: players) {
                if (player.getId() != user.getId()) {
                    if (player.getCharacter().getPos().distanceTo(new Point(Window.getWidth()/2, Window.getHeight()/2)) < 100){
                        if (!player.getCharacter().isDead()) {
                            player.getCharacter().setDead(true);
                        }
                    }
                }
            }
        }
        this.timer--;
        if (this.timer <= 0) {
            this.activating = false;
            this.radius = 0;
        }
    }
    public void warp(ArrayList<Player> players, Player user) {
        double magneticPower = 3;
        for (Player player: players) {
            if (user.getId() != player.getId()) {
                if (player.getCharacter().getPos().x < Window.getWidth()/2) {
                    player.getCharacter().setPosition(new Point(player.getCharacter().getPos().x + magneticPower * (player.getCharacter().getPos().x/Window.getWidth()/2) , player.getCharacter().getPos().y));
                }
                else {
                    player.getCharacter().setPosition(new Point(player.getCharacter().getPos().x - magneticPower * (player.getCharacter().getPos().x/Window.getWidth()/2), player.getCharacter().getPos().y));
                }
                if (player.getCharacter().getPos().y < Window.getHeight()/2) {
                    player.getCharacter().setPosition(new Point(player.getCharacter().getPos().x, player.getCharacter().getPos().y + magneticPower * (player.getCharacter().getPos().y/Window.getHeight()/2)));
                }
                else {
                    player.getCharacter().setPosition(new Point(player.getCharacter().getPos().x, player.getCharacter().getPos().y - magneticPower *(player.getCharacter().getPos().y/Window.getHeight()/2)));
                }
            }
        }
    }

    public void renderAbility() {
        if (radius < Window.getWidth()/1.5) {
            radius += 2;
        }
        if (timer > 6 * frames) {
            Drawing.drawRectangle(0,0,Window.getWidth(),Window.getHeight(), new Colour(0,0,0,0.9));
            Image gojo = new Image("res/sidecharacters/Gojo/special2.png");
            gojo.drawFromTopLeft(0,0);
            if (radius < Window.getWidth()/1.5) {
                Drawing.drawCircle(Window.getWidth()/2, Window.getHeight()/2, radius, new Colour(0,0,0));
                radius += 3;
            }
        }
        else {
            Drawing.drawRectangle(0,0,Window.getWidth(),Window.getHeight(), new Colour(0,0,0,0.9));
            Drawing.drawCircle(new Point(Window.getWidth()/2, Window.getHeight()/2), 50, new Colour(1, 1, 1, 1));
            if (timer % 10 == 0) {
                Drawing.drawCircle(new Point(Window.getWidth()/2, Window.getHeight()/2), 600, new Colour(0.5, 0, 0, 0.4));
            }
            else if (timer % 14 == 0) {
                Drawing.drawCircle(new Point(Window.getWidth()/2, Window.getHeight()/2), 400, new Colour(0, 0, 0.5, 0.6));
            }
            else if (timer % 16 == 0) {
                Drawing.drawCircle(new Point(Window.getWidth()/2, Window.getHeight()/2), 200, new Colour(0.5, 0, 0.5, 0.7));
            }
            else if (timer % 17 == 0) {
                Drawing.drawCircle(new Point(Window.getWidth()/2, Window.getHeight()/2), 100, new Colour(0.5, 0, 0.5, 0.9));
            }
        }
    }
}
