import Enums.TileType;
import bagel.Image;
import bagel.util.Point;

import java.util.ArrayList;

public class TileBasicLeft extends Tile {

    private ArrayList<CollisionBlock> collisionBlocks = new ArrayList<>();
    private Point pos;
    private Image image = new Image("res/Tiles/BasicTile.png");
    private final TileType type = TileType.BASICLEFT;

    TileBasicLeft(Point point) {
        this.pos = point;
        collisionBlocks.add(new CollisionBlock(point, 14, image.getHeight()));
        if (SettingsSingleton.getInstance().isNight()) {
            image = new Image("res/Tiles/BasicTileNight.png");
        }
        else {
            image = new Image("res/Tiles/BasicTile.png");
        }
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
            block.draw();
        }
    };


    public Image getImage() {
        return this.image;
    }
    public TileType getType() {return this.type;}
}
