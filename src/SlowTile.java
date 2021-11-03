import bagel.Image;
import bagel.util.Point;

import java.util.ArrayList;

public class SlowTile extends Tile {

    String name = "Slow";
    ArrayList<CollisionBlock> collisionBlocks = new ArrayList<>();
    Point pos;
    Image image = new Image("res/Tiles/BasicTile.png");

    SlowTile(Point point) {
        super(point);
        this.pos = point;
        if ((java.time.LocalTime.now().getHour() > 18) || (java.time.LocalTime.now().getHour() < 4)) {
            image = new Image("res/Tiles/SlowTileNight.png");
        }
        else {
            image = new Image("res/Tiles/SlowTile.png");
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
            block.updatePos(this.pos);
            block.draw();
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
