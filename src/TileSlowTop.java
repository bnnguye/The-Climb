import Enums.TileType;
import bagel.Image;
import bagel.util.Point;

import java.util.ArrayList;

public class TileSlowTop extends Tile {

    private final TileType type = TileType.SLOWTOP;
    private final ArrayList<CollisionBlock> collisionBlocks = new ArrayList<>();
    private Point pos;
    private final Image image;

    TileSlowTop(Point point) {
        this.pos = point;

        if ((java.time.LocalTime.now().getHour() > 18) || (java.time.LocalTime.now().getHour() < 4)) {
            image = new Image("res/Tiles/SlowTileNight.png");
        }
        else {
            image = new Image("res/Tiles/SlowTile.png");
        }

        collisionBlocks.add(new CollisionBlock(point, image.getWidth() , 14));
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
        }
    }

    public void draw() {
        this.image.drawFromTopLeft(this.pos.x, this.pos.y);
        for (CollisionBlock block: this.collisionBlocks) {
            block.updatePos(this.pos);
            block.draw();
        }
    }

    public Image getImage() {
        return this.image;
    }
    public TileType getType() {return this.type;}
}
