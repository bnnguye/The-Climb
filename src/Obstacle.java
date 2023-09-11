import bagel.util.Point;
import bagel.util.Rectangle;

import java.util.ArrayList;

public abstract class Obstacle {
    protected ImagePoint image;
    protected double offset = 0;
    protected final ArrayList<Integer> playersInteracted = new ArrayList<>();

    abstract void move();
    abstract ImagePoint getImage();
    abstract Rectangle getBoundingBox();
    abstract Point getPos();
    abstract void setPos(Point point);

    public void adjustOffset(double offset) { this.offset += offset;}
    public void setOffset(double offset) {this.offset = offset;}
    abstract double getSpeed();
    abstract void draw();
    abstract void draw(double x, double y);
    abstract void collide(Character character);


    public double getOffset() {
        return offset;
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
}
