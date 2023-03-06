import bagel.Image;

import java.util.ArrayList;

public class SideAllMight extends SideCharacter{
    private final double frames = SettingsSingleton.getInstance().getRefreshRate();

    private String name = CharacterNames.ALLMIGHT;
    private String power = "ONE FOR ALL";
    private String desc = "All Might's One For All allows All Might to take over\n" +
            " the opponents on the battlefield through his immense will \n" +
            "and power. Gain enhanced movement speed and invulnerability\n" +
            " to objects flying " +
            "from the sky for a brief period.";

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


    public void activateAbility(Player user, ArrayList<Obstacle> obstacles, ArrayList<PowerUp> powerUps) {
        if(!this.activating) {
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

