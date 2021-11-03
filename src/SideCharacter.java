import bagel.Image;
import bagel.Input;
import bagel.util.Point;

import java.util.ArrayList;

public abstract class SideCharacter {
    private final int frames = 144;
    String name;
    Image icon;
    Point iconPos;
    boolean activating;

    public boolean isAnimating() {
        return animating;
    }


    boolean animating;
    Music music = new Music();
    Image selected;

    public String getName() {
        return name;
    }

    public void activateAbility(Player player, ArrayList<Player> players, ArrayList<Obstacle> obstacles) {}

    public void playLine() {}

    public Image getIcon() {return icon;}
    public void setIconPos(Point point) {this.iconPos = point;}
    public Point getIconPos() {return iconPos;}
    public Image getSelected() {return selected;}
    public boolean isActivating() {return activating;}
    public void reset() {}
    public void stopMusic() {}

    public void setMap(Map map){}
    public void setPowerUps(ArrayList<PowerUp> powerUps) {}
    public void setLeft(boolean bool) {}
}
