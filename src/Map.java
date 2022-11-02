import bagel.Image;
import bagel.Window;
import bagel.util.Point;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Map {
    private String name;
    Image mapPeek;
    double height;
    double currentHeight;
    private boolean jotaroAbility = false;

    ArrayList<Tile> tiles = new ArrayList<>();

    public Map(String name) {
        this.name = name;
        this.mapPeek = new Image(String.format("res/mapPeeks/%s.png", this.name));

    }

    public void draw() {
        for(Tile tile: tiles) {
            if ((tile.getPos().y < Window.getHeight() + tile.getImage().getHeight()) && (tile.getPos().y > -tile.getImage().getHeight())) {
                tile.draw();
            }
        }
    }

    public Image getMapPeek() {
        return mapPeek;
    }

    public String getName() {
        return name;
    }

    public void updateTiles(double shift) {
        if (!jotaroAbility) {
            currentHeight += shift;
            ArrayList<Tile>tilesToRemove = new ArrayList<>();
            for (Tile tile: tiles) {
                for (CollisionBlock block: tile.getCollisionBlocks()) {
                    block.updatePos(new Point (tile.getPos().x, tile.getPos().y + shift));
                }
                tile.setPos(new Point(tile.getPos().x, tile.getPos().y + shift));
                if (tile.getPos().y > Window.getHeight()) {
                    tilesToRemove.add(tile);
                }
            }
            tiles.removeAll(tilesToRemove);
        }
    }

    public boolean hasFinished() {
        if (currentHeight > height) {
            return true;
        }
        return false;
    }
    public void generateMap() {
        jotaroAbility = false;
        tiles.clear();
        int currentBlocksInRow = 0;
        int currentRow = 0;
        this.height = -Window.getHeight();
        this.currentHeight = 0;
        try {
            Scanner scanner = new Scanner(new File(String.format("res/mapData/%s.txt", this.name)));
            while(scanner.hasNextLine()) {
                String[] line = scanner.nextLine().split(",");
                for (String tileType: line) {
                    tiles.add(tileChooser(tileType, new Point(480 * currentBlocksInRow, 600 + -475 * currentRow)));
                    currentBlocksInRow++;
                    if (currentBlocksInRow > 3) {
                        currentBlocksInRow = 0;
                        this.height += 475;
                        currentRow++;
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

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
        else if (line.equals("IceLeft")) {
            tile = new TileIceLeft(point);
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
    public ArrayList<Tile> getTiles() {return tiles;}
    public ArrayList<Tile> getVisibleTiles() {
        ArrayList<Tile> visibleTiles = new ArrayList<>();
        for(Tile tile: this.tiles) {
            if ((tile.getPos().y < Window.getHeight() + tile.getImage().getHeight()) && (tile.getPos().y > -tile.getImage().getHeight())) {
                visibleTiles.add(tile);
            }
        }
        return visibleTiles;
    }
    public double getHeight() {
        return height;
    }

    public double getCurrentHeight() {
        return currentHeight;
    }

    public void setJotaroAbility(boolean bool) {
        jotaroAbility = bool;
    }

    public boolean getJotaroAbility() {
        return jotaroAbility;
    }
}
