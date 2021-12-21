import bagel.Drawing;
import bagel.Image;
import bagel.Window;
import bagel.util.Colour;
import bagel.util.Point;
import bagel.util.Rectangle;

import java.util.ArrayList;

public class SideJotaro extends SideCharacter{
    private final int frames = 144;
    String name = "Jotaro";
    String soundPath = String.format("music/%s.wav", this.name);
    Image icon = new Image(String.format("res/charactersS/%s/Icon.PNG", this.name));
    boolean activating = false;
    int timer = 0;
    Image selected = new Image(String.format("res/Selected/%s_Selected.png", this.name));

    int shakeTimer;
    ArrayList<PowerUp> powerUps;
    Map map;

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

    public void activateAbility(Player user,ArrayList<Player> players, ArrayList<Obstacle> obstacles) {

        Colour darken = new Colour(0, 0, 0.2, 0.5);
        Drawing.drawRectangle(0, 0, Window.getWidth(), Window.getHeight(), darken);
        if (!this.activating) {
            timer = 5*frames;
            this.activating = true;
        }
        else {
            if (timer > 3*frames) {
                this.animating = true;
                Image noblePhantasm = new Image("res/charactersS/Jotaro/NoblePhantasm.png");
                noblePhantasm.drawFromTopLeft(0,0);
            }
            else {
                this.animating = false;
                for (PowerUp powerUp: powerUps) {
                    powerUp.setJotaroAbility(true);
                }
                for(Obstacle obstacle: obstacles) {
                    obstacle.setJotaroAbility(true);
                }
                for (Player player: players) {
                    if (player.getId() != user.getId()) {
                        player.getCharacter().setJotaroAbility(true);
                    }
                }
                this.map.setJotaroAbility(true);
            }
            timer--;
        }
        if (timer <= 0) {
            for (PowerUp powerUp: powerUps) {
                powerUp.setJotaroAbility(false);
            }
            for(Obstacle obstacle: obstacles) {
                obstacle.setJotaroAbility(false);
            }
            for (Player player: players) {
                if (player.getId() != user.getId()) {
                    player.getCharacter().setJotaroAbility(false);
                }
            }
            this.map.setJotaroAbility(false);
            this.activating = false;
        }
    }



    public boolean isAnimating() {
        return this.animating;
    }
    public void setPowerUps(ArrayList<PowerUp> powerUps) {this.powerUps = powerUps;}
    public void setMap(Map map) {this.map = map;}

}