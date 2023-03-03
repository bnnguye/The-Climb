import bagel.Drawing;
import bagel.Image;
import bagel.Window;
import bagel.util.Colour;
import bagel.util.Point;

import java.util.ArrayList;

public class SideYuu extends SideCharacter{
    private final double frames = SettingsSingleton.getInstance().getRefreshRate();

    private String name = CharacterNames.YUU;
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
            MusicPlayer.getInstance().addMusic("music/Yuu.wav");
            this.activating = true;
            this.timer = 2 * frames;
        }
        if (timer > 0 * frames) {
            this.animating = true;
        }
        else if (timer == 0) {
            temporarySideCharacter = getSideCharacter(findClosest(user, SettingsSingleton.getInstance().getPlayers()));
            if (temporarySideCharacter != null) {
                temporarySideCharacter.activateAbility(user, obstacles, powerUps);
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
                temporarySideCharacter.activateAbility(user, obstacles, powerUps);
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
        if (player.getSideCharacter().getName().equals(CharacterNames.DIO)) {
            playerCharacter = new SideDio();
        }
        else if (player.getSideCharacter().getName().equals(CharacterNames.ALLMIGHT)) {
            playerCharacter = new SideAllMight();
        }
        else if (player.getSideCharacter().getName().equals(CharacterNames.GOJO)) {
            playerCharacter = new SideGojo();
        }
        else if (player.getSideCharacter().getName().equals(CharacterNames.HISOKA)) {
            playerCharacter = new SideHisoka();
        }
        else if (player.getSideCharacter().getName().equals(CharacterNames.ITACHI)) {
            playerCharacter = new SideItachi();
        }
        else if (player.getSideCharacter().getName().equals(CharacterNames.JOTARO)) {
            playerCharacter = new SideJotaro();
        }
        else if (player.getSideCharacter().getName().equals(CharacterNames.LELOUCH)) {
            playerCharacter = new SideLelouch();
        }
        else if (player.getSideCharacter().getName().equals(CharacterNames.GOKU)) {
            playerCharacter = new SideGoku();
        }
        else if (player.getSideCharacter().getName().equals(CharacterNames.YUGI)) {
            playerCharacter = new SideYugi();
        }
        else if (player.getSideCharacter().getName().equals(CharacterNames.ZORO)) {
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
                    closestDistance = (Math.abs(user.getCharacter().getPos().x - player.getCharacter().getPos().x) + Math.abs(user.getCharacter().getPos().y - player.getCharacter().getPos().y));
                } else {
                    if ((Math.abs(user.getCharacter().getPos().x - player.getCharacter().getPos().x) + Math.abs(user.getCharacter().getPos().y - player.getCharacter().getPos().y)) < closestDistance) {
                        closest = player;
                        closestDistance = (Math.abs(user.getCharacter().getPos().x - player.getCharacter().getPos().x) + Math.abs(user.getCharacter().getPos().y - player.getCharacter().getPos().y));
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
