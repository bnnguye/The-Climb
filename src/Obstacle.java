import bagel.Image;
import bagel.Window;
import bagel.util.Point;

import java.util.ArrayList;

public abstract class Obstacle {
    private String name = "";
    Image image;
    private Point pos;
    double speed;
    private boolean jotaroAbility = false;
    private ArrayList<Integer> playersGainedEXP = new ArrayList<>();

    public Obstacle() {
        pos = new Point(Window.getWidth() * Math.random(), -200);
    }

    public void move() {
    }

    public Image getImage() { return image;}
    public Point getPos() {return pos;}
    public void setPos(Point point) { pos = point;}
    public String getName() {return "";}
    public void setJotaroAbility(boolean bool) {
        jotaroAbility = bool;
    }
    public boolean canMove() {
        return !jotaroAbility;
    }

    public ArrayList<Integer> getPlayersGainedEXP() {
        return playersGainedEXP;
    }
    public void updatePlayersGainedEXP(Integer playerId) {
        playersGainedEXP.add(playerId);
    }
}
