import Enums.TileType;
import bagel.Image;
import bagel.util.Point;

import java.util.ArrayList;

public abstract class Tile {

    abstract Image getImage();

    abstract ArrayList<CollisionBlock> getCollisionBlocks();

    abstract Point getPos();

    abstract void setPos(Point pos);

    abstract void draw();

    abstract TileType getType();
}
