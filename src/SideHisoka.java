import bagel.Drawing;
import bagel.Image;
import bagel.Window;
import bagel.util.Colour;
import bagel.util.Point;
import java.util.ArrayList;

public class SideHisoka extends SideCharacter{
    private final double frames = SettingsSingleton.getInstance().getFrames();
    private String name = "Hisoka";
    private String soundPath = String.format("music/%s.wav", this.name);
    Image icon = new Image(String.format("res/charactersS/%s/Icon.PNG", this.name));
    boolean activating = false;
    double timer;
    Image selected = new Image(String.format("res/Selected/%s_Selected.png", this.name));

    boolean shoot = false;
    ArrayList<ObstacleBungeeGum> bungeeGums;
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
            this.activating = true;
            this.timer = 10 * frames;
            bungeeGums = new ArrayList<>();
            bungeeGums.add(new ObstacleBungeeGum(user.getCharacter().getPos(), "N"));
            bungeeGums.add(new ObstacleBungeeGum(user.getCharacter().getPos(), "S"));
            bungeeGums.add(new ObstacleBungeeGum(user.getCharacter().getPos(), "E"));
            bungeeGums.add(new ObstacleBungeeGum(user.getCharacter().getPos(), "W"));
            bungeeGums.add(new ObstacleBungeeGum(user.getCharacter().getPos(), "NW"));
            bungeeGums.add(new ObstacleBungeeGum(user.getCharacter().getPos(), "NE"));
            bungeeGums.add(new ObstacleBungeeGum(user.getCharacter().getPos(), "SW"));
            bungeeGums.add(new ObstacleBungeeGum(user.getCharacter().getPos(), "SE"));
        }

        if (timer > 8*frames) {
            Drawing.drawRectangle(0, 0, Window.getWidth(), Window.getHeight(), new Colour(0, 0, 0, 0.5));
            Image noblePhantasm = new Image("res/charactersS/Hisoka/NoblePhantasm.png");
            Colour darken = new Colour(0, 0, 0, 0.5);
            Drawing.drawRectangle(0, 0, Window.getWidth(), Window.getHeight(), darken);
            noblePhantasm.drawFromTopLeft(0,0);
            this.animating = true;
        }
        else {
            this.animating = false;
        }
        this.timer--;
        if (timer < 8*frames) {
            if (bungeeGums.size() > 0) {
                ArrayList<ObstacleBungeeGum> bgToRemove = new ArrayList<>();
                for (ObstacleBungeeGum bg: bungeeGums) {
                    bg.draw();
                    bg.move();
                    for(Player player: players) {
                        if (player.getId() != user.getId()) {
                            if (player.getCharacter().getImage().getBoundingBoxAt(player.getCharacter().getPos()).intersects(bg.getPos())) {
                                if (!player.isDead()) {
                                    player.getCharacter().setHisokaAbility(4 * frames);
                                    bgToRemove.add(bg);
                                }
                            }
                        }
                    }
                    if ((bg.getPos().y > Window.getHeight()) || (bg.getPos().y < 0)) {
                        bgToRemove.add(bg);
                    }
                    else if ((bg.getPos().x > Window.getWidth()) || (bg.getPos().x < 0)) {
                        bgToRemove.add(bg);
                    }
                }
                bungeeGums.removeAll(bgToRemove);
            }
            else {
                this.activating = false;
            }
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
}
