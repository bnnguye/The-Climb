import bagel.Image;
import bagel.util.Point;

import java.util.ArrayList;

public class SideGoku extends SideCharacter{
    private final double frames = TimeLogger.getInstance().getRefreshRate();

    private final String name = CharacterNames.GOKU;
    private final String power = "ULTRA INSTINCT";
    private final String desc = "Son Goku reaches his final evolution \"Ultra Instinct\", becoming ...TBA";

    boolean activating = false;
    boolean animating = false;
    double timer;

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


    public void activateAbility(Player user) {
        if(!this.activating) {
            MusicPlayer.getInstance().addMusic(getSoundPath());
            this.activating = true;
            this.timer = 10 * frames;
        }

        if (this.timer > 8*frames) {
            this.animating = true;
        }
        else {
            this.animating = false;
            user.getCharacter().setAllMightAbility();
        }
        this.timer--;
        if (this.timer <= 0) {
            this.activating = false;
        }
    }

    public void renderAbility() {
        if (timer > 8 * frames) {
            Image special = new Image(String.format("res/sidecharacters/%s/special.png", this.name));
            special.drawFromTopLeft(0,0);
        }
    }

}

