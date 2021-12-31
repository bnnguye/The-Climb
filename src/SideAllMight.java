import bagel.Image;
import bagel.util.Point;

import java.util.ArrayList;

public class SideAllMight extends SideCharacter{
    private final double frames = SettingsSingleton.getInstance().getFrames();
    String name = "AllMight";
    String soundPath = String.format("music/%s.wav", this.name);
    Image icon = new Image(String.format("res/charactersS/%s/Icon.PNG", this.name));
    boolean activating = false;
    boolean animating = false;
    double timer;
    Music music = new Music();
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
        if(!this.activating) {
            this.music.playMusic("music/AllMight.wav");
            this.music.played = true;
            this.activating = true;
            this.timer = 10 * frames;
        }

        if (this.timer > 8*frames) {
            Image noblePhantasm = new Image(String.format("res/charactersS/%s/NoblePhantasm.png", this.name));
            noblePhantasm.drawFromTopLeft(0,0);
            this.animating = true;
        }
        else {
            this.animating = false;
            user.getCharacter().setAllMightAbility();
        }
        this.timer--;
        if (this.timer <= 0) {
            this.activating = false;
            this.music.played = false;
        }
    }

    public void reset() {
        this.activating = false;
        this.animating = false;
        if (this.music.played) {
            this.music.stopMusic();
        }
        timer = 0;
    }

    public void shoot() {}
    public boolean isAnimating() {
        return this.animating;
    }
}

