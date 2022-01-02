import bagel.Image;
import bagel.util.Point;

import java.util.ArrayList;

public class TileIceTop extends Tile {

    private String name = "IceTop";
    String type = "Ice";
    ArrayList<CollisionBlock> collisionBlocks = new ArrayList<>();
    private Point pos;
    Image image = new Image("res/Tiles/BasicTile.png");

    TileIceTop(Point point) {
        super(point);
        this.pos = point;
        collisionBlocks.add(new CollisionBlock(point, image.getWidth() , 14));
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
