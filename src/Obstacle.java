import bagel.Image;
import bagel.Window;
import bagel.util.Point;

import java.util.ArrayList;

public abstract class Obstacle {
    Image image;
    private Point pos;
    private final ArrayList<Integer> playersInteracted = new ArrayList<>();
    private double speed;
    protected double offset = 0;

    public Obstacle() {
        pos = new Point(Window.getWidth() * Math.random(), -200);
    }

    public void move() {
    }

    public Image getImage() { return image;}
    public Point getPos() {return pos;}
    public void setPos(Point point) { pos = point;}
    public String getName() {return "";}
    public void adjustOffset(double offset) {
        this.offset += offset;
    }
    public double getSpeed() {
        return speed;
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
