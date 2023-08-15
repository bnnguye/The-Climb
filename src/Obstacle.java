import Enums.Obstacles;
import bagel.Image;
import bagel.Window;
import bagel.util.Point;
import bagel.util.Rectangle;

import java.util.ArrayList;

public abstract class Obstacle {
    protected Point pos;
    protected double offset = 0;
    protected final ArrayList<Integer> playersInteracted = new ArrayList<>();

    public Obstacle() {
        pos = new Point(Window.getWidth() * Math.random(), -200);
    }

    abstract void move();
    abstract Image getImage();
    abstract Rectangle getBoundingBox();
    abstract Point getPos();
    abstract void setPos(Point point);
    abstract Obstacles getType();
    abstract void adjustOffset(double offset);
    abstract double getSpeed();
    abstract void draw();
    abstract void draw(double x, double y);
    abstract void collide(Character character);

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
}
