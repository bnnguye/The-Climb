import bagel.Drawing;
import bagel.Image;
import bagel.Window;
import bagel.util.Colour;
import bagel.util.Point;
import bagel.util.Rectangle;

import java.util.ArrayList;

public class SideLelouch extends SideCharacter{
    private final double frames = SettingsSingleton.getInstance().getRefreshRate();

    private String name = CharacterNames.LELOUCH;
    private String power = "CODE GEASS";
    private String desc = "";

    boolean activating = false;
    boolean animating = false;
    double timer;

    boolean shoot = false;
    private Player user;

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
        this.timer = 0;
        this.shoot = false;
    }


    public void activateAbility(Player user, ArrayList<Obstacle> obstacles, ArrayList<PowerUp> powerUps) {
        if(!this.activating) {
            MusicPlayer.getInstance().addMusic(getSoundPath());
            this.user = user;
            this.activating = true;
            this.timer = 6 * frames;
        }

        if (timer > 1*frames) {
            Drawing.drawRectangle(0, 0, Window.getWidth(), Window.getHeight(), new Colour(0, 0, 0, 0.5));
            this.animating = true;
        }
        else {
            this.animating = false;
        }
        this.timer--;
        if (this.timer <= 0) {
            Rectangle vertical = new Rectangle(user.getCharacter().getPos().x, 0, 2, Window.getHeight());
            Rectangle horizontal = new Rectangle(0, user.getCharacter().getPos().y, Window.getWidth(), 2);
            for (Player player: SettingsSingleton.getInstance().getPlayers()) {
                if((player.getCharacter().getImage().getBoundingBoxAt(player.getCharacter().getPos()).intersects(horizontal)) || (player.getCharacter().getImage().getBoundingBoxAt(player.getCharacter().getPos()).intersects(vertical))) {
                    if (player.getId() != user.getId()) {
                        if (!player.getCharacter().isDead()) {
                            player.getCharacter().setLives(0);
                        }
                    }
                }
            }
            this.activating = false;
            this.shoot = false;
        }

    }

    public void renderAbility() {
        if (timer > 1 *frames) {
            Image special = new Image("res/sidecharacters/Lelouch Lamperouge/special.png");
            special.drawFromTopLeft(0,0);
        }
        else {
            Image eye = new Image("res/sidecharacters/Lelouch Lamperouge/Eye.png");
            eye.drawFromTopLeft(0,0);
            Colour red = new Colour(0.8, 0, 0, 0.5);
            Drawing.drawRectangle(user.getCharacter().getPos().x, 0, 2, Window.getHeight(), red);
            Drawing.drawRectangle(0, user.getCharacter().getPos().y, Window.getWidth(), 2, red);
        }
    }
}
