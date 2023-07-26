import Enums.TileType;
import bagel.Image;
import bagel.util.Point;

import java.util.ArrayList;

public class TileSlow extends Tile {

    private final TileType type = TileType.SLOW;
    private final ArrayList<CollisionBlock> collisionBlocks = new ArrayList<>();
    private Point pos;
    private final Image image;

    TileSlow(Point point) {
        this.pos = point;

        if ((java.time.LocalTime.now().getHour() > 18) || (java.time.LocalTime.now().getHour() < 4)) {
            image = new Image("res/Tiles/SlowTileNight.png");
        }
        else {
            image = new Image("res/Tiles/SlowTile.png");
        }
    }

    public ArrayList<CollisionBlock> getCollisionBlocks() {
        return collisionBlocks;
    }

    public Point getPos() {
        return this.pos;
    }

    public void setPos(Point pos) {
        this.pos = pos;
        for (CollisionBlock block: this.collisionBlocks) {
            block.setPos(this.pos);
            block.draw();
        }
    }

    public void draw() {
        this.image.drawFromTopLeft(this.pos.x, this.pos.y);
    }

    public Image getImage() {
        return this.image;
    }
    public TileType getType() {return this.type;}
}
