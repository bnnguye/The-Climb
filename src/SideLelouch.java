import bagel.Drawing;
import bagel.Image;
import bagel.Window;
import bagel.util.Colour;
import bagel.util.Point;
import bagel.util.Rectangle;

import java.util.ArrayList;

public class SideLelouch extends SideCharacter{
    private final double frames = SettingsSingleton.getInstance().getFrames();
    private String name = "Lelouch";
    private String soundPath = String.format("music/%s.wav", this.name);
    Image icon = new Image(String.format("res/charactersS/%s/Icon.PNG", this.name));
    boolean activating = false;
    boolean animating = false;
    double timer;
    private Image selected = new Image(String.format("res/charactersS/%s/Selected.png", this.name));
    private Player user;

    boolean shoot = false;
    private Point iconPos;


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
            for (Player player: players) {
                if((player.getCharacter().getImage().getBoundingBoxAt(player.getCharacter().getPos()).intersects(horizontal)) || (player.getCharacter().getImage().getBoundingBoxAt(player.getCharacter().getPos()).intersects(vertical))) {
                    if (player.getId() != user.getId()) {
                        if (!player.isDead()) {
                            player.setDead();
                        }
                    }
                }
            }
            this.activating = false;
            this.shoot = false;
        }

    }

    public void reset() {
        this.activating = false;
        this.animating = false;
        this.timer = 0;
        this.shoot = false;
    }

    public boolean isAnimating() {
        return this.animating;
    }

    public void renderAbility() {
        if (timer > 1 *frames) {
            Image noblePhantasm = new Image("res/charactersS/Lelouch/NoblePhantasm.png");
            noblePhantasm.drawFromTopLeft(0,0);
        }
        else {
            Image eye = new Image("res/charactersS/Lelouch/Eye.png");
            eye.drawFromTopLeft(0,0);
            Colour red = new Colour(0.8, 0, 0, 0.5);
            Drawing.drawRectangle(user.getCharacter().getPos().x, 0, 2, Window.getHeight(), red);
            Drawing.drawRectangle(0, user.getCharacter().getPos().y, Window.getWidth(), 2, red);
        }
    }
    public String getSoundPath() {return soundPath;}
}
