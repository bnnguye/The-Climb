import bagel.Drawing;
import bagel.Image;
import bagel.Window;
import bagel.util.Colour;
import bagel.util.Point;

import java.util.ArrayList;

public class SideJotaro extends SideCharacter{
    private final double frames = SettingsSingleton.getInstance().getRefreshRate();
    private String name = "Jotaro";
    private String soundPath = String.format("music/%s.wav", this.name);
    Image icon = new Image(String.format("res/sidecharacters/%s/Icon.PNG", this.name));
    boolean activating = false;
    double timer;
    private Image selected = new Image(String.format("res/sidecharacters/%s/Selected.png", this.name));

    int shakeTimer;
    ArrayList<PowerUp> powerUps;
    private Point iconPos;

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
        if (!this.activating) {
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



    public boolean isAnimating() {
        return this.animating;
    }
    public void setPowerUps(ArrayList<PowerUp> powerUps) {this.powerUps = powerUps;}

    public void renderAbility() {
        Colour darken = new Colour(0, 0, 0.2, 0.5);
        Drawing.drawRectangle(0, 0, Window.getWidth(), Window.getHeight(), darken);
        if (timer > 3*frames) {
            Image special = new Image("res/sidecharacters/Jotaro/special.png");
            special.drawFromTopLeft(0,0);
        }
    }

    public String getSoundPath() {return soundPath;}

}