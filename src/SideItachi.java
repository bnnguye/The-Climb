import bagel.Drawing;
import bagel.Image;
import bagel.Input;
import bagel.Window;
import bagel.util.Colour;
import bagel.util.Point;

import java.util.ArrayList;

public class SideItachi extends SideCharacter{
    private final int frames = 144;
    String name = "Itachi";
    String soundPath = String.format("music/%s.wav", this.name);
    Image icon = new Image(String.format("res/charactersS/%s/Icon.PNG", this.name));
    boolean activating = false;
    int timer = 0;
    Image selected = new Image(String.format("res/Selected/%s_Selected.png", this.name));

    ArrayList<PowerUp> powerUps;
    Map map;
    boolean left = true;
    Player user;

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

    public void activateAbility(Player user,ArrayList<Player> players, ArrayList<Obstacle> obstacles) {
        this.user = user;
        Colour red = new Colour(0.7, 0, 0, 0.5);
        Drawing.drawRectangle(0, 0, Window.getWidth(), Window.getHeight(), red);
        Colour darken = new Colour(0, 0, 0, 0.5);
        Drawing.drawRectangle(0, 0, Window.getWidth(), Window.getHeight(), darken);
        if (!this.activating) {
            timer = 8*frames;
            this.activating = true;
        }
        else {
            if (timer > 6*frames) {
                this.animating = true;
                Image noblePhantasm = new Image("res/charactersS/Itachi/NoblePhantasm.png");
                noblePhantasm.drawFromTopLeft(0,0);
            }
            else {
                this.animating = false;
                for (PowerUp powerUp: powerUps) {
                    powerUp.getImage().drawFromTopLeft(powerUp.getPos().x - 50, powerUp.getPos().y);
                    powerUp.getImage().drawFromTopLeft(powerUp.getPos().x + 50, powerUp.getPos().y);
                }
                for(Obstacle obstacle: obstacles) {
                    if (this.left) {
                        obstacle.getImage().drawFromTopLeft(obstacle.getPos().x - 50, obstacle.getPos().y);
                    }
                    else {
                        obstacle.getImage().drawFromTopLeft(obstacle.getPos().x + 50, obstacle.getPos().y);
                    }
                }
                for (Player player: players) {
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

    public boolean isAnimating() {
        return this.animating;
    }
    public void setPowerUps(ArrayList<PowerUp> powerUps) {this.powerUps = powerUps;}
    public void setMap(Map map) {this.map = map;}
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

}