import bagel.Image;
import bagel.util.Point;

import java.util.ArrayList;

public abstract class Tile {
    private String name= "";
    String type = "";
    ArrayList<CollisionBlock> collisionBlocks;
    Image image;
    private Point pos;

    Tile(Point point) {
        pos = point;
    }

    public Image getImage() {
        return image;
    }

    public ArrayList<CollisionBlock> getCollisionBlocks() {
        return collisionBlocks;
    }

    public void setCollisionBlocks(ArrayList<CollisionBlock> collisionBlocks) {
    }

    public Point getPos() {
        return pos;
    }

    public void setPos(Point pos) {
    }

    public void draw() {
    }

    public String getName() { return name;}
    public String getType() {return type;}
}
