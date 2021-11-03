import bagel.Drawing;
import bagel.Image;
import bagel.Window;
import bagel.util.Colour;
import bagel.util.Point;
import java.util.ArrayList;

public class SideHisoka extends SideCharacter{
    private final int frames = 144;
    String name = "Hisoka";
    Image icon = new Image(String.format("res/charactersS/%s/Icon.PNG", this.name));
    boolean activating = false;
    boolean animating = false;
    int timer;
    Music music = new Music();
    Image selected = new Image(String.format("res/Selected/%s_Selected.png", this.name));
    boolean shoot = false;
    ArrayList<BungeeGum> bungeeGums;


    public String getName() {
        return this.name;
    }
    public Image getIcon() {return this.icon;}
    public void setIconPos(Point point) {this.iconPos = point;}
    public Point getIconPos() {return this.iconPos;}
    public Image getSelected() {return this.selected;}
    public boolean isActivating() {return this.activating;}

    public void activateAbility(Player user,ArrayList<Player> players, ArrayList<Obstacle> obstacles) {
        if(!this.activating) {
            this.music.playMusic("music/Hisoka.wav");
            this.music.played = true;
            this.activating = true;
            this.timer = 10 * frames;
            bungeeGums = new ArrayList<>();
            bungeeGums.add(new BungeeGum(user.getCharacter().getPos(), "N"));
            bungeeGums.add(new BungeeGum(user.getCharacter().getPos(), "S"));
            bungeeGums.add(new BungeeGum(user.getCharacter().getPos(), "E"));
            bungeeGums.add(new BungeeGum(user.getCharacter().getPos(), "W"));
            bungeeGums.add(new BungeeGum(user.getCharacter().getPos(), "NW"));
            bungeeGums.add(new BungeeGum(user.getCharacter().getPos(), "NE"));
            bungeeGums.add(new BungeeGum(user.getCharacter().getPos(), "SW"));
            bungeeGums.add(new BungeeGum(user.getCharacter().getPos(), "SE"));
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
        if (this.timer <= 0) {
            this.music.played = false;
        }
        if (timer < 8*frames) {
            if (bungeeGums.size() > 0) {
                ArrayList<BungeeGum> bgToRemove = new ArrayList<>();
                for (BungeeGum bg: bungeeGums) {
                    bg.draw();
                    bg.move();
                    for(Player player: players) {
                        if (player.getId() != user.getId()) {
                            if (player.getCharacter().getImage().getBoundingBoxAt(player.getCharacter().getPos()).intersects(bg.getPos())) {
                                if (!player.isDead()) {
                                    player.getCharacter().setHisokaAbility(true);
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
                stopMusic();
                this.activating = false;
            }
        }
    }

    public void reset() {
        stopMusic();
        this.activating = false;
        this.animating = false;
        this.timer = 0;
        this.shoot = false;
    }

    public void stopMusic() {
        if (this.music.played) {
            this.music.stopMusic();
        }
    }
    public boolean isAnimating() {
        return this.animating;
    }
}
