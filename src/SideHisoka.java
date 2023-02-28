import bagel.Drawing;
import bagel.Image;
import bagel.Window;
import bagel.util.Colour;
import bagel.util.Point;
import java.util.ArrayList;

public class SideHisoka extends SideCharacter{
    private final double frames = SettingsSingleton.getInstance().getRefreshRate();

    private String name = CharacterNames.HISOKA;
    private String power = "BUNGEE GUM";
    private String desc = "Hisoka Morow's signature move \"Bungee Gum\" allows him to trap his opponents by converting" +
            " his aura into a sticky substance, freezing any character caught within its path for several seconds.";

    boolean activating = false;
    boolean animating = false;
    double timer;

    boolean shoot = false;
    ArrayList<ObstacleBungeeGum> bungeeGums;

    public String getName() {
        return this.name;
    }
    public String getPower() { return this.power;}
    public String getDesc() { return this.desc;}
    public String getSoundPath() {return String.format("music/%s.wav", this.name);}

    public boolean isActivating() {return this.activating;}
    public boolean isAnimating() {
        return this.animating;
    }
    public void reset() {
        this.activating = false;
        this.animating = false;
        timer = 0;
    }


    public void activateAbility(Player user, ArrayList<Player> players, ArrayList<Obstacle> obstacles, ArrayList<PowerUp> powerUps, Map map) {
        if(!this.activating) {
            MusicPlayer.getInstance().addMusic("music/Hisoka.wav");
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
                    bg.move();
                    for(Player player: players) {
                        if (player.getId() != user.getId()) {
                            if (player.getCharacter().getImage().getBoundingBoxAt(player.getCharacter().getPos())
                                    .intersects(bg.getImage().getBoundingBoxAt(new Point(bg.getPos().x -
                                            bg.getImage().getWidth()/2, bg.getPos().y - bg.getImage().getHeight()/2)))) {
                                if (!player.getCharacter().isDead()) {
                                    player.getCharacter().gotStunned();
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

    public void renderAbility() {
        if (timer > 8*frames) {
            Drawing.drawRectangle(0, 0, Window.getWidth(), Window.getHeight(), new Colour(0, 0, 0, 0.5));
            Image special = new Image("res/sidecharacters/Hisoka/special.png");
            Colour darken = new Colour(0, 0, 0, 0.5);
            Drawing.drawRectangle(0, 0, Window.getWidth(), Window.getHeight(), darken);
            special.drawFromTopLeft(0,0);
        }
        else {
            for (ObstacleBungeeGum bg: bungeeGums) {
                bg.draw();
            }
        }
    }
}
