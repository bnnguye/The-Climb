import bagel.Drawing;
import bagel.Image;
import bagel.Input;
import bagel.Window;
import bagel.util.Colour;
import bagel.util.Point;

import java.util.ArrayList;

public class SideItachi extends SideCharacter{
    private final double frames = SettingsSingleton.getInstance().getFrames();
    private String name = "Itachi";
    private String soundPath = String.format("music/%s.wav", this.name);
    Image icon = new Image(String.format("res/charactersS/%s/Icon.PNG", this.name));
    boolean activating = false;
    double timer;
    private Image selected = new Image(String.format("res/charactersS/%s/Selected.png", this.name));

    private ArrayList<Obstacle> obstacles;
    private ArrayList<PowerUp> powerUps;
    private boolean left = true;
    private Player user;
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
    }
    public String playLine() {return this.soundPath;}

    public void activateAbility(Player user, ArrayList<Player> players, ArrayList<Obstacle> obstacles, ArrayList<PowerUp> powerUps, Map map) {
        this.obstacles = obstacles;
        this.powerUps = powerUps;
        this.user = user;
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
                for (Player player: players) {
                    if (player.getId() != user.getId()) {
                        player.getCharacter().onSlow();
                    }
                }
            }
            if (!map.isJotaroAbility()) {
                timer--;
            }
        }
        if (timer <= 0) {
            this.activating = false;
        }
    }

    public boolean isAnimating() {
        return this.animating;
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
            Image noblePhantasm = new Image("res/charactersS/Itachi/NoblePhantasm.png");
            noblePhantasm.drawFromTopLeft(0,0);
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
    public String getSoundPath() {return soundPath;}


}