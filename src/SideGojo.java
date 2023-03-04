import bagel.Drawing;
import bagel.Image;
import bagel.Window;
import bagel.util.Colour;
import bagel.util.Point;

import java.util.ArrayList;

public class SideGojo extends SideCharacter{
    private final double frames = SettingsSingleton.getInstance().getRefreshRate();
    private String name = CharacterNames.GOJO;
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
    public String getSoundPath() {return String.format("music/sidecharacters/%s/%s.wav", this.name, this.name);}

    public boolean isActivating() {return this.activating;}
    public boolean isAnimating() {
        return this.animating;
    }
    public void reset() {
        this.activating = false;
        this.animating = false;
        timer = 0;
    }


    public void activateAbility(Player user, ArrayList<Obstacle> obstacles, ArrayList<PowerUp> powerUps) {
        if(!this.activating) {
            MusicPlayer.getInstance().addMusic(getSoundPath());
            this.activating = true;
            this.timer = 6 * frames;
        }

        if (this.timer > 4*frames) {
            this.animating = true;
        }
        else {
            this.animating = false;
            warp(SettingsSingleton.getInstance().getPlayers(), user);
            for (Player player: SettingsSingleton.getInstance().getPlayers()) {
                if (player.getId() != user.getId()) {
                    if (player.getCharacter().getPos().distanceTo(new Point(Window.getWidth()/2, Window.getHeight()/2)) < 100){
                        if (!player.getCharacter().isDead()) {
                            player.getCharacter().setLives(0);
                        }
                    }
                }
            }
        }
        this.timer--;
        if (this.timer <= 0) {
            MusicPlayer.getInstance().remove(getSoundPath());
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
        if (timer > 4 * frames) {
            Drawing.drawRectangle(0,0,Window.getWidth(),Window.getHeight(), new Colour(0,0,0,0.9));
            Image gojo = new Image(String.format("res/sidecharacters/%s/NoblePhantasm2.png", getName()));
            gojo.drawFromTopLeft(0,0);
        }
        else {
            if (radius < Window.getWidth()/1.5) {
                Drawing.drawCircle(Window.getWidth()/2, Window.getHeight()/2, radius, new Colour(0,0,0));
                radius += 3;
            }
            else {
                Drawing.drawRectangle(0,0,Window.getWidth(),Window.getHeight(), new Colour(0,0,0,0.5));
            }
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
