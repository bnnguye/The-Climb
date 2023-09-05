import Enums.TileType;
import bagel.Image;
import bagel.util.Point;

import java.util.ArrayList;

public class TileBasicSad extends Tile {
    
    private final TileType type = TileType.BASICSAD;
    private ArrayList<CollisionBlock> collisionBlocks = new ArrayList<>();
    private final Image image;
    private Point pos;

    TileBasicSad(Point point) {
        this.pos = point;

        if (SettingsSingleton.getInstance().isNight()) {
            image = new Image("res/Tiles/BasicTileNight.png");
        }
        else {
            image = new Image("res/Tiles/BasicTile.png");
        }

        collisionBlocks.add(new CollisionBlock(point, image.getWidth() , 14));
        collisionBlocks.add(new CollisionBlock(new Point(point.x + image.getWidth()-14, point.y), 14 , image.getHeight()));
        collisionBlocks.add(new CollisionBlock(point, 14 , image.getHeight()));
    }

    public ArrayList<CollisionBlock> getCollisionBlocks() {
        return collisionBlocks;
    }

    public void setCollisionBlocks(ArrayList<CollisionBlock> collisionBlocks) {
        this.collisionBlocks = collisionBlocks;
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
            block.updatePos(this.pos);
            block.draw();
        }
    };

    public Image getImage() {
        return this.image;
    }
    public TileType getType() {return this.type;}
}
