import bagel.Image;
import bagel.util.Point;

import java.util.ArrayList;

public class TileIce extends Tile {

    String name = "Ice";
    ArrayList<CollisionBlock> collisionBlocks = new ArrayList<>();
    Point pos;
    Image image = new Image("res/Tiles/BasicTile.png");

    TileIce(Point point) {
        super(point);
        this.pos = point;
        if ((java.time.LocalTime.now().getHour() > 18) || (java.time.LocalTime.now().getHour() < 4)) {
            image = new Image("res/Tiles/IceTileNight.png");
        }
        else {
            image = new Image("res/Tiles/IceTile.png");
        }
    }

    @Override
    public ArrayList<CollisionBlock> getCollisionBlocks() {
        return collisionBlocks;
    }

    @Override
    public void setCollisionBlocks(ArrayList<CollisionBlock> collisionBlocks) {
        this.collisionBlocks = collisionBlocks;
    }

    @Override
    public Point getPos() {
        return this.pos;
    }

    @Override
    public void setPos(Point pos) {
        this.pos = pos;
        for (CollisionBlock block: this.collisionBlocks) {
            block.setPos(this.pos);
        }
    }

    public void draw() {
        this.image.drawFromTopLeft(this.pos.x, this.pos.y);
    }

    public Image getImage() {
        return this.image;
    }

    public String getName() {return this.name;}
}
