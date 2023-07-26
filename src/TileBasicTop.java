import Enums.TileType;
import bagel.Image;
import bagel.util.Point;

import java.util.ArrayList;

public class TileBasicTop extends Tile {

    private final TileType type = TileType.BASICTOP;
    private final ArrayList<CollisionBlock> collisionBlocks = new ArrayList<>();
    private final Image image;
    private Point pos;

    TileBasicTop(Point point) {
        this.pos = point;

        if ((java.time.LocalTime.now().getHour() > 18) || (java.time.LocalTime.now().getHour() < 4)) {
            image = new Image("res/Tiles/BasicTileNight.png");
        }
        else {
            image = new Image("res/Tiles/BasicTile.png");
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
            block.updatePos(new Point(this.pos.x - 100, this.pos.y));
            block.draw();
        }
    }
    public Image getImage() {
        return this.image;
    }
    public TileType getType() {return this.type;}
}
