import bagel.Drawing;
import bagel.Image;
import bagel.Window;
import bagel.util.Colour;
import bagel.util.Point;

import java.util.ArrayList;

public class SideJotaro extends SideCharacter{
    private final double frames = SettingsSingleton.getInstance().getRefreshRate();

    private String name = CharacterNames.JOTARO;
    private String power = "STAR PLATINUM: THE WORLD";
    private String desc = "Jotaro calls his Stand \"Star Platinum\" out and uses his ultimate\n" +
            "ability, \"The World\", stopping time in its tracks for several seconds.\n" +
            "This unique ability was curated within the Kujo blood, however rumors\n" +
            "say a certain vampire has honed this ability as well,\n" +
            "with goals of someday taking over the world.";


    boolean activating = false;
    double timer;

    ArrayList<PowerUp> powerUps;

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
        if (!this.activating) {
            MusicPlayer.getInstance().addMusic(getSoundPath());
            timer = 5*frames;
            this.activating = true;
        }
        else {
            if (timer > 3*frames) {
                this.animating = true;
            }
            else {
                for (Obstacle obstacle: obstacles) {
                    obstacle.setJotaroAbility(true);
                }
                for (PowerUp powerUp: powerUps) {
                    powerUp.setJotaroAbility(true);
                }
                this.animating = false;
            }
            timer--;
        }
        if (timer <= 0) {
            for (Obstacle obstacle : obstacles) {
                obstacle.setJotaroAbility(false);
            }
            for (PowerUp powerUp : powerUps) {
                powerUp.setJotaroAbility(false);
            }
            this.activating = false;
        }
    }

    public void setPowerUps(ArrayList<PowerUp> powerUps) {this.powerUps = powerUps;}

    public void renderAbility() {
        Colour darken = new Colour(0, 0, 0.1, 0.7);
        Drawing.drawRectangle(0, 0, Window.getWidth(), Window.getHeight(), darken);
        if (timer > 3*frames) {
            Image special = new Image("res/sidecharacters/JOTARO KUJO/render.png");
            special.drawFromTopLeft(Window.getWidth()/2 - special.getWidth()/2,Window.getHeight() - special.getHeight());
        }
    }


}