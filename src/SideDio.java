import bagel.Drawing;
import bagel.Image;
import bagel.Window;
import bagel.util.Colour;
import bagel.util.Point;
import bagel.util.Rectangle;

import java.util.ArrayList;

public class SideDio extends SideCharacter{
    private final double frames = SettingsSingleton.getInstance().getFrames();
    private String name = "Dio";
    private String soundPath = String.format("music/%s.wav", this.name);
    Image icon = new Image(String.format("res/charactersS/%s/Icon.PNG", this.name));
    boolean activating = false;
    double timer;
    private Image selected = new Image(String.format("res/charactersS/%s/Selected.png", this.name));

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
                this.animating = false;
                for (Player player: players) {
                    if (player.getId() != user.getId()) {
                        player.getCharacter().setJotaroAbility(true);
                    }
                    if (player.getSideCharacter().isActivating()) {
                        if (player.getSideCharacter().getName().equals("Jotaro")) {
                            player.getCharacter().setJotaroAbility(false);
                        }
                    }
                }
                for (Obstacle obstacle: obstacles) {
                    obstacle.setJotaroAbility(true);
                }
                for (PowerUp powerUp: powerUps) {
                    powerUp.setJotaroAbility(true);
                }
                map.setJotaroAbility(true);
            }
            timer--;
        }
        if (timer <= 0) {
            for (Player player : players) {
                if (player.getId() != user.getId()) {
                    player.getCharacter().setJotaroAbility(false);
                }
            }
            for (Obstacle obstacle : obstacles) {
                obstacle.setJotaroAbility(false);
            }
            for (PowerUp powerUp : powerUps) {
                powerUp.setJotaroAbility(false);
            }
            map.setJotaroAbility(false);
            this.activating = false;
        }
    }

    public void renderAbility() {
        Colour darken = new Colour(0, 0, 0.2, 0.5);
        Drawing.drawRectangle(0, 0, Window.getWidth(), Window.getHeight(), darken);
        if (timer > 3*frames) {
             Image noblePhantasm = new Image("res/charactersS/Dio/NoblePhantasm.png");
             noblePhantasm.drawFromTopLeft(0,0);
        }
    }



    public boolean isAnimating() {
        return this.animating;
    }
    public String getSoundPath() {return soundPath;}
    public void setPowerUps(ArrayList<PowerUp> powerUps) {this.powerUps = powerUps;}

}