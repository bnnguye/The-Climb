import bagel.Image;
import bagel.util.Point;

import java.util.ArrayList;

public class TileBasicSad extends Tile {

    String name = "BasicSad";
    String type = "Basic";
    ArrayList<CollisionBlock> collisionBlocks = new ArrayList<>();
    Image image = new Image("res/Tiles/BasicTile.png");
    Point pos;

    TileBasicSad(Point point) {
        super(point);
        this.pos = point;
        collisionBlocks.add(new CollisionBlock(point, image.getWidth() , 14));
        collisionBlocks.add(new CollisionBlock(new Point(point.x + image.getWidth()-14, point.y), 14 , image.getHeight()));
        collisionBlocks.add(new CollisionBlock(point, 14 , image.getHeight()));
        if ((java.time.LocalTime.now().getHour() > 18) || (java.time.LocalTime.now().getHour() < 4)) {
            image = new Image("res/Tiles/BasicTileNight.png");
        }
        else {
            image = new Image("res/Tiles/BasicTile.png");
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
        for (CollisionBlock block: this.collisionBlocks) {
            block.updatePos(this.pos);
            block.draw();
        }
    }

    public Image getImage() {
        return this.image;
    }
    public String getName() {return this.name;}
    public String getType() {return this.type;}
}
