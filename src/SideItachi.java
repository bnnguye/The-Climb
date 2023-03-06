import bagel.Drawing;
import bagel.Image;
import bagel.Window;
import bagel.util.Colour;
import bagel.util.Point;

import java.util.ArrayList;

public class SideItachi extends SideCharacter{
    private final double frames = SettingsSingleton.getInstance().getRefreshRate();

    private String name = CharacterNames.ITACHI;
    private String power = "INFINITE TSUKUYOMI";
    private String desc = "Itachi activates his unique ability \"Infinite Tsukuyomi\"\n," +
            " sending all those who look into his left eye into a hallucination.\n" +
            " Those strong enough will be sent into a state of confusion, while\n" +
            "the rest have their lives in the hands of the shinobi.";

    boolean activating = false;
    double timer;

    private ArrayList<Obstacle> obstacles;
    private ArrayList<PowerUp> powerUps;
    private boolean left = true;

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
        this.obstacles = obstacles;
        this.powerUps = powerUps;
        MusicPlayer.getInstance().addMusic(getSoundPath());
        if (!this.activating) {
            timer = 8*frames;
            this.activating = true;
        }
        else {
            if (timer > 4*frames) {
                this.animating = true;
            }
            else {
                this.animating = false;
                for (Player player: SettingsSingleton.getInstance().getPlayers()) {
                    if (player.getId() != user.getId()) {
                        player.getCharacter().onSlow();
                    }
                }
            }
            timer--;
        }
        if (timer <= 0) {
            this.activating = false;
        }
    }

    public void setPowerUps(ArrayList<PowerUp> powerUps) {this.powerUps = powerUps;}
    public void setLeft(boolean left) {
        if (this.animating) {
            if (left) {
                this.left = true;
            }
            else {
                this.left = false;
            }
        }
    }

    public void renderAbility() {
        if (timer > 4*frames) {
            Colour darken = new Colour(0, 0, 0, 0.5);
            Drawing.drawRectangle(0, 0, Window.getWidth(), Window.getHeight(), darken);
            Image special = new Image("res/sidecharacters/Itachi/special.png");
            special.drawFromTopLeft(0,0);
        }
        else {
            for(Obstacle obstacle: obstacles) {
                if (this.left) {
                    obstacle.getImage().drawFromTopLeft(obstacle.getPos().x - 50, obstacle.getPos().y);
                }
                else {
                    obstacle.getImage().drawFromTopLeft(obstacle.getPos().x + 50, obstacle.getPos().y);
                }
            }
            for (PowerUp powerUp: powerUps) {
                if (this.left) {
                    powerUp.getImage().drawFromTopLeft(powerUp.getPos().x - 50, powerUp.getPos().y);
                }
                else {
                    powerUp.getImage().drawFromTopLeft(powerUp.getPos().x + 50, powerUp.getPos().y);
                }
            }
            Colour red = new Colour(0.7, 0, 0, 0.5);
            Drawing.drawRectangle(0, 0, Window.getWidth(), Window.getHeight(), red);
            Colour darken = new Colour(0, 0, 0, 0.5);
            Drawing.drawRectangle(0, 0, Window.getWidth(), Window.getHeight(), darken);
        }
    }


}