import bagel.Drawing;
import bagel.Image;
import bagel.Window;
import bagel.util.Colour;
import bagel.util.Point;

import java.util.ArrayList;

public class SideYuu extends SideCharacter{
    private final double frames = SettingsSingleton.getInstance().getRefreshRate();

    private String name = "YUU OTASAKA";
    private String power = "PLUNDER";
    private String desc = "";

    boolean activating = false;
    boolean animating = false;
    double timer;

    SideCharacter temporarySideCharacter;

    public String getName() {
        return this.name;
    }
    public String getPower() { return this.power;}
    public String getDesc() { return this.desc;}

    public boolean isActivating() {return this.activating;}
    public boolean isAnimating() {
        return this.animating;
    }
    public void reset() {
        this.activating = false;
        this.animating = false;
        timer = 0;
    }
    public String getSoundPath() {return String.format("music/%s.wav", this.name);}

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

    public SideCharacter getSideCharacter(Player player) {
        SideCharacter playerCharacter = null;
        if (player.getSideCharacter().getName().equals("DIO BRANDO")) {
            playerCharacter = new SideDio();
        }
        else if (player.getSideCharacter().getName().equals("ALL MIGHT")) {
            playerCharacter = new SideAllMight();
        }
        else if (player.getSideCharacter().getName().equals("GOJO SATORU")) {
            playerCharacter = new SideGojo();
        }
        else if (player.getSideCharacter().getName().equals("HISOKA MOROW")) {
            playerCharacter = new SideHisoka();
        }
        else if (player.getSideCharacter().getName().equals("ITACHI UCHIHA")) {
            playerCharacter = new SideItachi();
        }
        else if (player.getSideCharacter().getName().equals("JOTARO KUJO")) {
            playerCharacter = new SideJotaro();
        }
        else if (player.getSideCharacter().getName().equals("LELOUCH LAMPEROUGE")) {
            playerCharacter = new SideLelouch();
        }
        else if (player.getSideCharacter().getName().equals("SON GOKU")) {
            playerCharacter = new SideGoku();
        }
        else if (player.getSideCharacter().getName().equals("Yugi")) {
            playerCharacter = new SideYugi();
        }
        else if (player.getSideCharacter().getName().equals("Zoro")) {
            playerCharacter = new SideZoro();
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
                    closestDistance = (Math.abs(user.getCursorPos().x - player.getCursorPos().x) + Math.abs(user.getCursorPos().y - player.getCursorPos().y));
                } else {
                    if ((Math.abs(user.getCursorPos().x - player.getCursorPos().x) + Math.abs(user.getCursorPos().y - player.getCursorPos().y)) < closestDistance) {
                        closest = player;
                        closestDistance = (Math.abs(user.getCursorPos().x - player.getCursorPos().x) + Math.abs(user.getCursorPos().y - player.getCursorPos().y));
                    }
                }
            }
        }
        return closest;
    }

    public void renderAbility() {
        if (timer > 0 ) {
            Drawing.drawRectangle(0, 0, Window.getWidth(), Window.getHeight(), new Colour(0, 0, 0, 0.5));
            Image special = new Image(String.format("res/sidecharacters/%s/special.png", this.name));
            special.drawFromTopLeft(0,0);
        }
        else {
            if (temporarySideCharacter != null) {
                temporarySideCharacter.renderAbility();
            }
        }
    }

}
