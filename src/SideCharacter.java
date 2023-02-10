import bagel.Image;
import bagel.util.Point;

import java.util.ArrayList;

public abstract class SideCharacter {
    private final double frames = SettingsSingleton.getInstance().getRefreshRate();
    private String name;
    private String soundPath;
    Image icon;
    private Point iconPos;
    boolean activating;
    boolean animating;
    Image selected;

    public boolean isAnimating() {
        return animating;
    }
    public String getName() {
        return name;
    }
    public String playLine() { return null;}
    public Image getIcon() {return icon;}
    public void setIconPos(Point point) {this.iconPos = point;}
    public Point getIconPos() {return iconPos;}
    public Image getSelected() {return selected;}
    public boolean isActivating() {return activating;}
    public void reset() {}

    public void activateAbility(Player user, ArrayList<Player> players, ArrayList<Obstacle> obstacles, ArrayList<PowerUp> powerUps, Map map) {}

    public void setMap(Map map){}
    public void setPowerUps(ArrayList<PowerUp> powerUps) {}
    public void setLeft(boolean bool) {}
    public ArrayList<ExodiaPiece> getExodiaPiecesCollected() { return null;}
    public void renderAbility() {}
    public String getSoundPath() {return soundPath;}
}
