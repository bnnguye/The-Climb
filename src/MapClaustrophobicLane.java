import bagel.Image;
import bagel.Window;
import bagel.util.Point;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class MapClaustrophobicLane extends Map {
    int level = 5;
    String name = "Claustrophobic Lane";
    Image mapPeek = new Image(String.format("res/mapPeeks/%d.png", this.level));
    Point pos;
    ArrayList<Tile> tiles = new ArrayList<>();
    double height = 0;
    double currentHeight = 0;
    boolean jotaroAbility = false;

    public Tile tileChooser(String line, Point point) {
        Tile tile = null;
        if (line.equals("Basic")) {
            tile = new TileBasic(point);
        }
        else if (line.equals("BasicTop")) {
            tile = new TileBasicTop(point);
        }
        else if (line.equals("BasicLeft")) {
            tile = new TileBasicLeft(point);
        }
        else if (line.equals("Ice")) {
            tile = new TileIce(point);
        }
        else if (line.equals("IceTop")) {
            tile = new TileIceTop(point);
        }
        else if (line.equals("Slow")) {
            tile = new TileSlow(point);
        }
        else if (line.equals("SlowLeft")) {
            tile = new TileSlowLeft(point);
        }
        else if (line.equals("BasicSad")) {
            tile = new TileBasicSad(point);
        }
        return tile;
    }

    public void draw() {
        for(Tile tile: tiles) {
            if ((tile.getPos().y < Window.getHeight()) && (tile.getPos().y > -tile.getImage().getHeight())) {
                tile.draw();
            }
        }
    }

    public Image getMapPeek() {
        return this.mapPeek;
    }

    public String getName() {
        return this.name;
    }

    public void setPos(Point point) {
        this.pos = point;
    }

    public int getLevel() {return this.level;}

    public void updateTiles(double shift) {
        if (!this.jotaroAbility) {
            currentHeight ++;
        }
        ArrayList<Tile>tilesToRemove = new ArrayList<>();
        if (!this.jotaroAbility) {
            for (Tile tile: tiles) {
                for (CollisionBlock block: tile.getCollisionBlocks()) {
                    block.updatePos(new Point (tile.getPos().x, tile.getPos().y + shift));
                }
                tile.setPos(new Point(tile.getPos().x, tile.getPos().y + shift));
                if (tile.getPos().y > Window.getHeight()) {
                    tilesToRemove.add(tile);
                }
            }
        }
        tiles.removeAll(tilesToRemove);
    }

    public boolean hasFinished() {
        return this.currentHeight >= this.height;
    }

    public void generateMap() {
        jotaroAbility = false;
        tiles.clear();
        int currentBlocksInRow = 0;
        int currentRow = 0;
        this.height = -1000;
        this.currentHeight = 0;
        try {
            Scanner scanner = new Scanner(new File("res/mapData/Claustrophobic Lane.txt"));
            while(scanner.hasNextLine()) {
                String[] line = scanner.nextLine().split(",");
                for (String tileType: line) {
                    tiles.add(tileChooser(tileType, new Point(480 * currentBlocksInRow, 600 + -475 * currentRow)));
                    currentBlocksInRow++;
                    if (currentBlocksInRow > 3) {
                        currentBlocksInRow = 0;
                        currentRow++;
                        this.height += 475;
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    public ArrayList<Tile> getTiles() {return this.tiles;}
    public ArrayList<Tile> getVisibleTiles() {
        ArrayList<Tile> visibleTiles = new ArrayList<>();
        for(Tile tile: this.tiles) {
            if ((tile.getPos().y < Window.getHeight()) && (tile.getPos().y > -tile.getImage().getHeight())) {
                visibleTiles.add(tile);
            }
        }
        return visibleTiles;
    }
    public double getHeight() {
        return this.height;
    }

    public double getCurrentHeight() {
        return this.currentHeight;
    }
    public void setJotaroAbility(boolean bool) { this.jotaroAbility = bool;}
}
