import Enums.TileType;
import bagel.Image;
import bagel.util.Point;

import java.util.ArrayList;

public class TileSlowLeft extends Tile {

    private final ArrayList<CollisionBlock> collisionBlocks = new ArrayList<>();
    private Point pos;
    private final Image image;
    private final TileType type = TileType.SLOWLEFT;

    TileSlowLeft(Point point) {
        this.pos = point;

        if (SettingsSingleton.getInstance().isNight()) {
            image = new Image("res/Tiles/SlowTileNight.png");
        }
        else {
            image = new Image("res/Tiles/SlowTile.png");
        }

        collisionBlocks.add(new CollisionBlock(point, 14, image.getHeight()));
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
    }

    public void drawCollisionBlocks(){
        for (CollisionBlock block: this.collisionBlocks) {
            block.draw();
        }
    };

    public Image getImage() {
        return this.image;
    }
    public TileType getType() {return this.type;}
}
