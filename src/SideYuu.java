import bagel.Drawing;
import bagel.Image;
import bagel.Window;
import bagel.util.Colour;
import bagel.util.Point;
import org.lwjgl.system.CallbackI;

import java.util.ArrayList;

public class SideYuu extends SideCharacter{
    private final double frames = SettingsSingleton.getInstance().getFrames();
    private String name = "Yuu";
    private String soundPath = String.format("music/%s.wav", this.name);
    Image icon = new Image(String.format("res/charactersS/%s/Icon.PNG", this.name));
    boolean activating = false;
    boolean animating = false;
    double timer;
    private Image selected = new Image(String.format("res/charactersS/%s/Selected.png", this.name));
    private Point iconPos;
    SideCharacter temporarySideCharacter;

    public String getName() {
        return this.name;
    }
    public Image getIcon() {return this.icon;}
    public void setIconPos(Point point) {this.iconPos = point;}
    public Point getIconPos() {return this.iconPos;}
    public Image getSelected() {return this.selected;}
    public boolean isActivating() {return this.activating;}
    public String playLine() {return this.soundPath;}

    public void activateAbility(Player user, ArrayList<Player> players, ArrayList<Obstacle> obstacles, ArrayList<PowerUp> powerUps, Map map) {
        if (!this.activating) {
            this.activating = true;
            this.timer = 2 * frames;
        }
        if (timer > 0 * frames) {
            this.animating = true;
        }
        else if (timer == 0) {
            temporarySideCharacter = getSideCharacter(findClosest(user, players));
            if (temporarySideCharacter != null) {
                temporarySideCharacter.activateAbility(user, players, obstacles, powerUps, map);
            }
            else {
                this.activating = false;
            }
        }
        else {
            if (temporarySideCharacter == null) {
                this.activating = false;
            }
            else {
                temporarySideCharacter.activateAbility(user, players, obstacles, powerUps, map);
                if (!temporarySideCharacter.isAnimating()) {
                    this.animating = false;
                }
                if (!temporarySideCharacter.isActivating()) {
                    this.activating = false;
                }
            }
        }
        timer--;
    }

    public void reset() {
        this.activating = false;
        this.animating = false;
        this.timer = 0;
    }


    public boolean isAnimating() {
        return this.animating;
    }

    public SideCharacter getSideCharacter(Player player) {
        SideCharacter playerCharacter = null;
        if (player.getSideCharacter().getName().equals("Dio")) {
            playerCharacter = new SideDio();
        }
        else if (player.getSideCharacter().getName().equals("AllMight")) {
            playerCharacter = new SideAllMight();
        }
        else if (player.getSideCharacter().getName().equals("Gojo")) {
            playerCharacter = new SideGojo();
        }
        else if (player.getSideCharacter().getName().equals("Hisoka")) {
            playerCharacter = new SideHisoka();
        }
        else if (player.getSideCharacter().getName().equals("Itachi")) {
            playerCharacter = new SideItachi();
        }
        else if (player.getSideCharacter().getName().equals("Jotaro")) {
            playerCharacter = new SideJotaro();
        }
        else if (player.getSideCharacter().getName().equals("Lelouch")) {
            playerCharacter = new SideLelouch();
        }
        else if (player.getSideCharacter().getName().equals("Puck")) {
            playerCharacter = new SidePuck();
        }
        else if (player.getSideCharacter().getName().equals("Yugi")) {
            playerCharacter = new SideYugi();
        }
        else if (player.getSideCharacter().getName().equals("Zoro")) {
            playerCharacter = new SideZoro();
        }
        else if (player.getSideCharacter().getName().equals("Senkuu")) {
            playerCharacter = new SideSenkuu();
        }
        return playerCharacter;
    }

    public Player findClosest(Player user, ArrayList<Player> players) {
        Player closest = null;
        double closestDistance = 0;
        for (Player player : players) {
            if (player.getId() != user.getId()) {
                if (closest == null) {
                    closest = player;
                    closestDistance = (Math.abs(user.getPos().x - player.getPos().x) + Math.abs(user.getPos().y - player.getPos().y));
                } else {
                    if ((Math.abs(user.getPos().x - player.getPos().x) + Math.abs(user.getPos().y - player.getPos().y)) < closestDistance) {
                        closest = player;
                        closestDistance = (Math.abs(user.getPos().x - player.getPos().x) + Math.abs(user.getPos().y - player.getPos().y));
                    }
                }
            }
        }
        return closest;
    }

    public void renderAbility() {
        if (timer > 0 ) {
            Drawing.drawRectangle(0, 0, Window.getWidth(), Window.getHeight(), new Colour(0, 0, 0, 0.5));
            Image noblePhantasm = new Image(String.format("res/charactersS/%s/SpecialAbilityPoints.png", this.name));
            noblePhantasm.drawFromTopLeft(0,0);
        }
        else {
            if (temporarySideCharacter != null) {
                temporarySideCharacter.renderAbility();
            }
        }
    }

    public String getSoundPath() {return soundPath;}

}
