import bagel.Drawing;
import bagel.Image;
import bagel.Window;
import bagel.util.Colour;
import bagel.util.Point;

import java.util.ArrayList;

public class SideDio extends SideCharacter{
    private final double frames = TimeLogger.getInstance().getRefreshRate();

    private final String name = CharacterNames.DIO;
    private final String power = "THE WORLD";
    private final String desc = "Dio's stand \"The World\", is a supernatural \n" +
            "ability, that allows the user to stop the tracks\n" +
            "of time for a few seconds, freezing all objects\n" +
            "on the entire battlefield. However, there exists a\n" +
            "special individual that rivals this demonic power.";

    boolean activating = false;
    boolean animating = false;

    double timer;

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

    public void setPowerUps(ArrayList<PowerUp> powerUps) {this.powerUps = powerUps;}
    public void reset() {
        this.activating = false;
        this.animating = false;
        this.timer = 0;
    }

    public void activateAbility(Player user) {
        if (!this.activating) {
            MusicPlayer.getInstance().addMusic("music/sidecharacters/DIO BRANDO/DIO BRANDO.wav");
            timer = 5 * frames;
            this.activating = true;
            for (Obstacle obstacle: obstacles) {
                obstacle.adjustOffset(-obstacle.getSpeed());
            }
            for (PowerUp powerUp: powerUps) {
                powerUp.adjustOffset(-powerUp.getSpeed());
            }
        }
        else {
            this.animating = timer > 3 * frames;
            timer--;
        }
        if (timer == 0) {
            this.activating = false;
            for (Obstacle obstacle: obstacles) {
                obstacle.setOffset(0);
            }
            for (PowerUp powerUp: powerUps) {
                powerUp.setOffset(0);
            }
        }
    }

    public void renderAbility() {
        Colour darken = new Colour(0, 0, 0.2, 0.5);
        Drawing.drawRectangle(0, 0, Window.getWidth(), Window.getHeight(), darken);
        if (timer > 3*frames) {
             Image special = new Image("res/sidecharacters/DIO BRANDO/special.png");
             special.drawFromTopLeft(0,0);
        }
    }

}