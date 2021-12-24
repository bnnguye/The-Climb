import bagel.Drawing;
import bagel.Image;
import bagel.Window;
import bagel.util.Colour;
import bagel.util.Point;

import java.util.ArrayList;

public class SideYuu extends SideCharacter{
    private final int frames = 144;
    String name = "Yuu";
    String soundPath = String.format("music/%s.wav", this.name);
    Image icon = new Image(String.format("res/charactersS/%s/Icon.PNG", this.name));
    boolean activating = false;
    boolean animating = false;
    int timer;
    Image selected = new Image(String.format("res/Selected/%s_Selected.png", this.name));

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
            Drawing.drawRectangle(0, 0, Window.getWidth(), Window.getHeight(), new Colour(0, 0, 0, 0.5));
            Image noblePhantasm = new Image(String.format("res/charactersS/%s/NoblePhantasm.png", this.name));
            noblePhantasm.drawFromTopLeft(0,0);
            this.animating = true;
        }
        else {
            this.animating = false;
            Player closest = null;
            double closestDistance = 0;
            for (Player player: players) {
                if (player != user) {
                    if (closest == null) {
                        closest = player;
                        closestDistance = (Math.abs(user.getPos().x - player.getPos().x) + Math.abs(user.getPos().y - player.getPos().y));
                    }
                    else {
                        if ((Math.abs(user.getPos().x - player.getPos().x) + Math.abs(user.getPos().y - player.getPos().y)) < closestDistance) {
                            closest = player;
                            closestDistance = (Math.abs(user.getPos().x - player.getPos().x) + Math.abs(user.getPos().y - player.getPos().y));
                        }
                    }
                }
            }
            replaceSideCharacter(user, closest);
            user.getSideCharacter().activateAbility(user, players, obstacles, powerUps, map);
            closest.getCharacter().useNoblePhantasm();
            this.activating = false;
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

    public void replaceSideCharacter(Player user, Player player) {
        SideCharacter playerCharacter = null;
        if (player.getSideCharacter().getName().equals("Dio")) {
            playerCharacter = new SideDio();
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
        user.setSideCharacter(playerCharacter);
        player.setSideCharacter(new SideYuu());
    }
}
