import bagel.Image;
import bagel.Window;
import bagel.util.Point;

import java.util.ArrayList;

public abstract class Obstacle {
    Image image;
    private Point pos;
    private boolean jotaroAbility = false;
    private final ArrayList<Integer> playersInteracted = new ArrayList<>();

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

    public ArrayList<Integer> getPlayersInteracted() {
        return playersInteracted;
    }
    public void updatePlayersInteracted(Integer playerId) {
        playersInteracted.add(playerId);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public double getX() {return this.pos.x;}
}
